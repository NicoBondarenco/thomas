package com.thomas.management.domain.adapter

import com.thomas.core.authorization.authorized
import com.thomas.core.extension.throws
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserBaseEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.UserService
import com.thomas.management.domain.event.UserEventProducer
import com.thomas.management.domain.exception.GroupListNotFoundException
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationDocumentNumberAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationMainEmailAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationPhoneNumberAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.model.extension.toUserDetailResponse
import com.thomas.management.domain.model.extension.toUserEntity
import com.thomas.management.domain.model.extension.toUserPageResponse
import com.thomas.management.domain.model.extension.toUserUpsertedEvent
import com.thomas.management.domain.model.extension.updateActive
import com.thomas.management.domain.model.extension.updateFromRequest
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserBaseRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.domain.userCreateRoles
import com.thomas.management.domain.userReadRoles
import com.thomas.management.domain.userUpdateRoles
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime
import java.util.UUID

class UserServiceAdapter(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val eventProducer: UserEventProducer,
) : UserService {

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<UserPageResponse> = authorized(roles = userReadRoles) {
        userRepository.page(
            keywordText,
            isActive,
            createdStart,
            createdEnd,
            updatedStart,
            updatedEnd,
            pageable
        )
    }.map { it.toUserPageResponse() }

    override fun one(
        id: UUID
    ): UserDetailResponse = authorized(roles = userReadRoles) {
        findByIdWithGroupsOrThrows(id).toUserDetailResponse()
    }

    override fun create(
        request: UserCreateRequest,
    ): UserDetailResponse = authorized(roles = userCreateRoles) {
        request.process(
            { req, groups -> req.toUserEntity(groups) },
            { user -> userRepository.create(user) },
            { eventProducer.sendCreatedEvent(it) },
        )
    }

    override fun update(
        id: UUID,
        request: UserUpdateRequest,
    ): UserDetailResponse = authorized(roles = userUpdateRoles) {
        request.process(
            { req, groups -> findByIdWithGroupsOrThrows(id).updateFromRequest(req, groups) },
            { userRepository.update(it) },
            { eventProducer.sendUpdatedEvent(it) },
        )
    }

    private fun <T : UserBaseRequest> T.process(
        entity: (T, Set<GroupEntity>) -> UserCompleteEntity,
        save: (UserCompleteEntity) -> UserCompleteEntity,
        produce: (UserUpsertedEvent) -> Unit,
    ): UserDetailResponse = this.let {
        val groups = this.userGroups.findGroupsByIds()
        entity(this, groups).apply {
            validateData()
            save(this)
        }
    }.apply {
        produce(this.toUserUpsertedEvent())
    }.toUserDetailResponse()

    private fun Set<UUID>.findGroupsByIds() = this.takeIf {
        it.isNotEmpty()
    }?.let {
        findGroups(this)
    } ?: setOf()

    override fun active(
        id: UUID,
        request: UserActiveRequest,
    ): UserPageResponse = authorized(roles = userUpdateRoles) {
        findByIdWithGroupsOrThrows(id).updateActive(request).apply {
            userRepository.update(this)
            eventProducer.sendUpdatedEvent(this.toUserUpsertedEvent())
        }.toUserPageResponse()
    }

    private fun findByIdWithGroupsOrThrows(
        id: UUID,
    ): UserCompleteEntity = userRepository.one(id)
        ?: throw UserNotFoundException(id)

    private fun UserCompleteEntity.validateData() = listOf<EntityValidation<UserBaseEntity>>(
        EntityValidation(
            UserBaseEntity::mainEmail.name.toSnakeCase(),
            { managementUserValidationMainEmailAlreadyUsed() },
            { !userRepository.hasAnotherWithSameMainEmail(this.id, this.mainEmail) }
        ),
        EntityValidation(
            UserBaseEntity::documentNumber.name.toSnakeCase(),
            { managementUserValidationDocumentNumberAlreadyUsed() },
            { !userRepository.hasAnotherWithSameDocumentNumber(this.id, this.documentNumber) }
        ),
        EntityValidation(
            UserBaseEntity::phoneNumber.name.toSnakeCase(),
            { managementUserValidationPhoneNumberAlreadyUsed() },
            { !(this.phoneNumber?.let { userRepository.hasAnotherWithSamePhoneNumber(this.id, it) } ?: false) }
        ),
    ).validate(this, managementUserValidationUserDataInvalidData())

    private fun findGroups(
        ids: Set<UUID>,
    ): Set<GroupEntity> = ids.let {
        groupRepository.allByIds(ids).apply {
            (ids subtract this.map { it.id }.toSet()).takeIf {
                it.isNotEmpty()
            }?.throws {
                GroupListNotFoundException(it)
            }
        }
    }

}
