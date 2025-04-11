package com.thomas.management.domain.adapter

import com.thomas.contract.messaging.management.ManagementEventType.CREATE
import com.thomas.contract.messaging.management.ManagementEventType.UPDATE
import com.thomas.core.aspect.MethodLog
import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.core.extension.validate
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.domain.UnitService
import com.thomas.management.domain.exception.UnitNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataInvalidData
import com.thomas.management.domain.messaging.event.UnitEventProducer
import com.thomas.management.domain.model.mapper.toUnitDataEvent
import com.thomas.management.domain.model.mapper.toUnitEntity
import com.thomas.management.domain.model.mapper.toUnitManagementEvent
import com.thomas.management.domain.model.mapper.toUnitResponse
import com.thomas.management.domain.model.mapper.updateFromRequest
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.UnitResponse
import com.thomas.management.domain.unitCreateRoles
import com.thomas.management.domain.unitDeleteRoles
import com.thomas.management.domain.unitReadRoles
import com.thomas.management.domain.unitUpdateRoles
import com.thomas.management.domain.validation.maxUnits
import com.thomas.management.domain.validation.sameName
import com.thomas.management.domain.validation.sameRegistration
import java.util.UUID

class UnitServiceAdapter(
    private val organizationRepository: OrganizationRepository,
    private val unitRepository: UnitRepository,
    private val unitEventProducer: UnitEventProducer,
) : UnitService {

    private val unitValidations = listOf(
        unitRepository.sameName(),
        unitRepository.sameRegistration(),
        unitRepository.maxUnits(),
    )

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod
    ): PageResponse<UnitResponse> = authorized(unitReadRoles) {
        unitRepository.page(
            organizationId = currentOrganization,
            keywordText = keywordText,
            isActive = isActive,
            pageable = pageable,
        ).map { it.toUnitResponse() }
    }

    override suspend fun one(
        id: UUID,
    ): UnitResponse = authorized(unitReadRoles) {
        findUnitByIdOrThrows(id).toUnitResponse()
    }

    @MethodLog
    override suspend fun create(
        request: UnitUpsertRequest
    ): UnitResponse = authorized(unitCreateRoles) {
        val organizationEntity = organizationRepository.one(currentOrganization)!!
        request.toUnitEntity(organizationEntity).upsert(
            { unitRepository.create(it) },
            { unitEventProducer.unitCreated(it.toUnitManagementEvent(CREATE)) }
        )
    }

    @MethodLog
    override suspend fun update(
        id: UUID,
        request: UnitUpsertRequest
    ): UnitResponse = authorized(unitUpdateRoles) {
        findUnitByIdOrThrows(id).updateFromRequest(request).upsert(
            { unitRepository.update(it) },
            { unitEventProducer.unitUpdated(it.toUnitManagementEvent(UPDATE)) }
        )
    }

    @MethodLog
    override suspend fun delete(
        id: UUID,
    ) = authorized(unitDeleteRoles) {
        unitRepository.delete(id)
        unitEventProducer.unitDeleted(id.toUnitManagementEvent())
    }

    private suspend fun UnitEntity.upsert(
        upsert: suspend (UnitEntity) -> UnitEntity,
        produce: suspend (UnitEntity) -> Unit,
    ) = this.let {
        unitValidations.validate(it, managementUnitValidationUnitDataInvalidData())
        upsert(it)
    }.apply {
        produce(this)
    }.toUnitResponse()

    private suspend fun findUnitByIdOrThrows(
        id: UUID,
    ): UnitEntity = unitRepository.one(id, currentOrganization)
        ?: throw UnitNotFoundException(id)

}
