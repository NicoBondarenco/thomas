package com.thomas.management.domain.adapter

import com.thomas.contract.messaging.management.ManagementEventType.CREATE
import com.thomas.contract.messaging.management.ManagementEventType.UPDATE
import com.thomas.core.aspect.AspectClass
import com.thomas.core.aspect.MethodLog
import com.thomas.core.authorization.authorized
import com.thomas.core.extension.validate
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.domain.OrganizationService
import com.thomas.management.domain.exception.OrganizationNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataInvalidData
import com.thomas.management.domain.messaging.event.OrganizationEventProducer
import com.thomas.management.domain.model.mapper.toOrganizationEntity
import com.thomas.management.domain.model.mapper.toOrganizationManagementEvent
import com.thomas.management.domain.model.mapper.toOrganizationResponse
import com.thomas.management.domain.model.mapper.updateFromRequest
import com.thomas.management.domain.model.request.OrganizationUpsertRequest
import com.thomas.management.domain.model.response.OrganizationResponse
import com.thomas.management.domain.organizationUpsertRoles
import com.thomas.management.domain.validation.sameName
import com.thomas.management.domain.validation.sameRegistration
import java.util.UUID

@AspectClass
class OrganizationServiceAdapter(
    private val organizationRepository: OrganizationRepository,
    private val organizationEventProducer: OrganizationEventProducer,
) : OrganizationService {

    private val organizationValidations = listOf(
        organizationRepository.sameName(),
        organizationRepository.sameRegistration(),
    )

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod
    ): PageResponse<OrganizationResponse> = authorized(organizationUpsertRoles) {
        organizationRepository.page(
            keywordText = keywordText,
            isActive = isActive,
            pageable = pageable,
        ).map { it.toOrganizationResponse() }
    }

    override suspend fun one(
        id: UUID,
    ): OrganizationResponse = authorized(organizationUpsertRoles) {
        findOrganizationByIdOrThrows(id).toOrganizationResponse()
    }

    @MethodLog
    override suspend fun create(
        request: OrganizationUpsertRequest
    ): OrganizationResponse = authorized(organizationUpsertRoles) {
        request.toOrganizationEntity().upsert(
            { organizationRepository.create(it) },
            { organizationEventProducer.organizationCreated(it.toOrganizationManagementEvent(CREATE)) }
        )
    }

    @MethodLog
    override suspend fun update(
        id: UUID,
        request: OrganizationUpsertRequest
    ): OrganizationResponse = authorized(organizationUpsertRoles) {
        findOrganizationByIdOrThrows(id).updateFromRequest(request).upsert(
            { organizationRepository.update(it) },
            { organizationEventProducer.organizationUpdated(it.toOrganizationManagementEvent(UPDATE)) }
        )
    }

    private suspend fun OrganizationEntity.upsert(
        upsert: suspend (OrganizationEntity) -> OrganizationEntity,
        produce: suspend (OrganizationEntity) -> Unit,
    ) = this.let {
        organizationValidations.validate(it, managementOrganizationValidationOrganizationDataInvalidData())
        upsert(it)
    }.apply {
        produce(this)
    }.toOrganizationResponse()

    private suspend fun findOrganizationByIdOrThrows(
        id: UUID,
    ): OrganizationEntity = organizationRepository.one(id)
        ?: throw OrganizationNotFoundException(id)

}
