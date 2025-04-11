package com.thomas.management.domain.adapter

import com.thomas.contract.messaging.management.ManagementEventType.CREATE
import com.thomas.contract.messaging.management.ManagementEventType.UPDATE
import com.thomas.core.aspect.MethodLog
import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.core.extension.validate
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.exception.GroupNotFoundException
import com.thomas.management.domain.groupCreateRoles
import com.thomas.management.domain.groupDeleteRoles
import com.thomas.management.domain.groupReadRoles
import com.thomas.management.domain.groupUpdateRoles
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupDataInvalidData
import com.thomas.management.domain.messaging.event.GroupEventProducer
import com.thomas.management.domain.model.mapper.toGroupCompleteEntity
import com.thomas.management.domain.model.mapper.toGroupDetailResponse
import com.thomas.management.domain.model.mapper.toGroupManagementEvent
import com.thomas.management.domain.model.mapper.toGroupSimpleResponse
import com.thomas.management.domain.model.mapper.toUnitRoleEntity
import com.thomas.management.domain.model.mapper.updateFromRequest
import com.thomas.management.domain.model.request.GroupUpsertRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupSimpleResponse
import com.thomas.management.domain.validation.groupUnitsFound
import com.thomas.management.domain.validation.sameName
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GroupServiceAdapter(
    private val organizationRepository: OrganizationRepository,
    private val groupRepository: GroupRepository,
    private val unitRepository: UnitRepository,
    private val groupProducer: GroupEventProducer,
) : GroupService {

    private fun groupValidations(
        groupUnits: Map<UUID, Set<SecurityUnitRole>>,
    ) = listOf(
        groupRepository.sameName(),
        groupUnitsFound(groupUnits),
    )

    private fun CoroutineScope.organizationDeferred() = async {
        organizationRepository.one(currentOrganization)!!
    }

    private fun CoroutineScope.unitsDeferred(request: GroupUpsertRequest) = async {
        val found = unitRepository.allByIds(request.groupUnits.keys, currentOrganization)
        found.associateWith { request.groupUnits[it.id]!!.toSet() }.toUnitRoleEntity()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun GroupUpsertRequest.toCompleteEntity(): GroupCompleteEntity = coroutineScope {
        val groupOrganization = organizationDeferred()
        val groupUnits = unitsDeferred(this@toCompleteEntity)

        listOf(
            groupOrganization,
            groupUnits,
        ).awaitAll()

        this@toCompleteEntity.toGroupCompleteEntity(
            groupOrganization.getCompleted(),
            groupUnits.getCompleted()
        )
    }

    private suspend fun GroupUpsertRequest.updateEntity(id: UUID): GroupCompleteEntity = coroutineScope {
        findByIdOrThrows(id).let { groupEntity ->
            val groupUnits = unitsDeferred(this@updateEntity).await()
            groupEntity.updateFromRequest(this@updateEntity, groupUnits)
        }
    }

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod
    ): PageResponse<GroupSimpleResponse> = authorized(groupReadRoles) {
        groupRepository.page(
            organizationId = currentOrganization,
            keywordText = keywordText,
            isActive = isActive,
            pageable = pageable,
        ).map { it.toGroupSimpleResponse() }
    }

    override suspend fun one(
        id: UUID,
    ): GroupDetailResponse = authorized(groupReadRoles) {
        findByIdOrThrows(id).toGroupDetailResponse()
    }

    @MethodLog
    override suspend fun create(
        request: GroupUpsertRequest
    ): GroupDetailResponse = authorized(groupCreateRoles) {
        request.toCompleteEntity().upsert(
            request,
            { groupRepository.create(it) },
            { groupProducer.groupCreated(it.toGroupManagementEvent(CREATE)) }
        )
    }

    @MethodLog
    override suspend fun update(
        id: UUID,
        request: GroupUpsertRequest
    ): GroupDetailResponse = authorized(groupUpdateRoles) {
        request.updateEntity(id).upsert(
            request,
            { groupRepository.update(it) },
            { groupProducer.groupUpdated(it.toGroupManagementEvent(UPDATE)) }
        )
    }

    @MethodLog
    override suspend fun delete(
        id: UUID,
    ) = authorized(groupDeleteRoles) {
        groupRepository.delete(id)
        groupProducer.groupDeleted(id.toGroupManagementEvent())
    }

    private suspend fun GroupCompleteEntity.upsert(
        request: GroupUpsertRequest,
        upsert: suspend (GroupCompleteEntity) -> GroupCompleteEntity,
        produce: suspend (GroupCompleteEntity) -> Unit,
    ) = this.let {
        groupValidations(
            groupUnits = request.groupUnits
        ).validate(it, managementGroupValidationGroupDataInvalidData())
        upsert(it)
    }.apply {
        produce(this)
    }.toGroupDetailResponse()

    private suspend fun findByIdOrThrows(
        id: UUID,
    ): GroupCompleteEntity = groupRepository.one(id, currentOrganization)
        ?: throw GroupNotFoundException(id)

}