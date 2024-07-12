package com.thomas.management.domain.adapter

import com.thomas.core.authorization.authorized
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupBaseEntity
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.domain.exception.GroupNotFoundException
import com.thomas.management.domain.groupCreateRoles
import com.thomas.management.domain.groupDeleteRoles
import com.thomas.management.domain.groupReadRoles
import com.thomas.management.domain.groupUpdateRoles
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupDataInvalidData
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupNameAlreadyUsed
import com.thomas.management.domain.model.extension.toGroupDetailResponse
import com.thomas.management.domain.model.extension.toGroupEntity
import com.thomas.management.domain.model.extension.toGroupPageResponse
import com.thomas.management.domain.model.extension.toGroupUpsertedEvent
import com.thomas.management.domain.model.extension.updateFromRequest
import com.thomas.management.domain.model.request.GroupActiveRequest
import com.thomas.management.domain.model.request.GroupCreateRequest
import com.thomas.management.domain.model.request.GroupUpdateRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupPageResponse
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import java.time.OffsetDateTime
import java.util.UUID

class GroupServiceAdapter(
    private val groupRepository: GroupRepository,
    private val eventProducer: GroupEventProducer,
) : GroupService {

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<GroupPageResponse> = authorized(roles = groupReadRoles) {
        groupRepository.page(
            keywordText,
            isActive,
            createdStart,
            createdEnd,
            updatedStart,
            updatedEnd,
            pageable
        )
    }.map { it.toGroupPageResponse() }

    override fun one(
        id: UUID
    ): GroupDetailResponse = authorized(roles = groupReadRoles) {
        findByIdOrThrows(id)
    }.toGroupDetailResponse()

    override fun create(
        request: GroupCreateRequest
    ): GroupDetailResponse = authorized(roles = groupCreateRoles) {
        request.toGroupEntity().process(
            { group -> groupRepository.create(group) },
            { eventProducer.sendCreatedEvent(it) },
        )
    }

    override fun update(
        id: UUID,
        request: GroupUpdateRequest
    ): GroupDetailResponse = authorized(roles = groupUpdateRoles) {
        findByIdOrThrows(id).updateFromRequest(request).process(
            { group -> groupRepository.update(group) },
            { eventProducer.sendUpdatedEvent(it) },
        )
    }

    private fun GroupCompleteEntity.process(
        save: (GroupCompleteEntity) -> GroupCompleteEntity,
        produce: (GroupUpsertedEvent) -> Unit,
    ): GroupDetailResponse = this.let {
        it.validateData()
        save(it)
    }.apply {
        produce(this.toGroupUpsertedEvent())
    }.toGroupDetailResponse()

    private fun GroupBaseEntity.validateData() = listOf<EntityValidation<GroupBaseEntity>>(
        EntityValidation(
            GroupBaseEntity::groupName.name.toSnakeCase(),
            { managementGroupValidationGroupNameAlreadyUsed() },
            { !groupRepository.hasAnotherWithSameGroupName(this.id, this.groupName) }
        )
    ).validate(this, managementGroupValidationGroupDataInvalidData())

    override fun delete(
        id: UUID
    ): Unit = authorized(roles = groupDeleteRoles) {
        findByIdOrThrows(id).apply {
            groupRepository.delete(id)
            eventProducer.sendDeletedEvent(GroupDeletedEvent(id))
        }
    }

    override fun active(
        id: UUID,
        request: GroupActiveRequest
    ): GroupPageResponse = authorized(roles = groupUpdateRoles) {
        findByIdOrThrows(id).updateFromRequest(request).apply {
            val updated = groupRepository.update(this)
            eventProducer.sendUpdatedEvent(updated.toGroupUpsertedEvent())
        }
    }.toGroupPageResponse()

    private fun findByIdOrThrows(
        id: UUID,
    ): GroupCompleteEntity = groupRepository.one(id)
        ?: throw GroupNotFoundException(id)

}
