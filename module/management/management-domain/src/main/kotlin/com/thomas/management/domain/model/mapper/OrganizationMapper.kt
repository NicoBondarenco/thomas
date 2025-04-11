package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.ManagementEventType
import com.thomas.contract.messaging.management.organization.OrganizationDataEvent
import com.thomas.contract.messaging.management.organization.OrganizationManagementEvent
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.domain.model.request.OrganizationUpsertRequest
import com.thomas.management.domain.model.request.SignupOrganizationRequest
import com.thomas.management.domain.model.response.OrganizationResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.coroutineScope

suspend fun SignupOrganizationRequest.toOrganizationEntity() = coroutineScope {
    OrganizationEntity(
        organizationName = this@toOrganizationEntity.organizationName,
        fantasyName = this@toOrganizationEntity.fantasyName,
        registrationNumber = this@toOrganizationEntity.registrationNumber,
        maximumUsers = this@toOrganizationEntity.maximumUsers,
        maximumUnits = this@toOrganizationEntity.maximumUnits,
        mainEmail = this@toOrganizationEntity.mainEmail,
        mainPhone = this@toOrganizationEntity.mainPhone,
        addressZipcode = this@toOrganizationEntity.addressZipcode,
        addressStreet = this@toOrganizationEntity.addressStreet,
        addressNumber = this@toOrganizationEntity.addressNumber,
        addressComplement = this@toOrganizationEntity.addressComplement,
        addressNeighborhood = this@toOrganizationEntity.addressNeighborhood,
        addressCity = this@toOrganizationEntity.addressCity,
        addressState = this@toOrganizationEntity.addressState,
    )
}

suspend fun OrganizationEntity.toOrganizationResponse() = coroutineScope {
    OrganizationResponse(
        id = this@toOrganizationResponse.id,
        organizationName = this@toOrganizationResponse.organizationName,
        fantasyName = this@toOrganizationResponse.fantasyName,
        registrationNumber = this@toOrganizationResponse.registrationNumber,
        maximumUsers = this@toOrganizationResponse.maximumUsers,
        maximumUnits = this@toOrganizationResponse.maximumUnits,
        mainEmail = this@toOrganizationResponse.mainEmail,
        mainPhone = this@toOrganizationResponse.mainPhone,
        addressZipcode = this@toOrganizationResponse.addressZipcode,
        addressStreet = this@toOrganizationResponse.addressStreet,
        addressNumber = this@toOrganizationResponse.addressNumber,
        addressComplement = this@toOrganizationResponse.addressComplement,
        addressNeighborhood = this@toOrganizationResponse.addressNeighborhood,
        addressCity = this@toOrganizationResponse.addressCity,
        addressState = this@toOrganizationResponse.addressState,
        isActive = this@toOrganizationResponse.isActive,
        createdAt = this@toOrganizationResponse.createdAt,
        updatedAt = this@toOrganizationResponse.updatedAt,
    )
}

suspend fun OrganizationUpsertRequest.toOrganizationEntity() = coroutineScope {
    OrganizationEntity(
        organizationName = this@toOrganizationEntity.organizationName,
        fantasyName = this@toOrganizationEntity.fantasyName,
        registrationNumber = this@toOrganizationEntity.registrationNumber,
        maximumUsers = this@toOrganizationEntity.maximumUsers,
        maximumUnits = this@toOrganizationEntity.maximumUnits,
        mainEmail = this@toOrganizationEntity.mainEmail,
        mainPhone = this@toOrganizationEntity.mainPhone,
        addressZipcode = this@toOrganizationEntity.addressZipcode,
        addressStreet = this@toOrganizationEntity.addressStreet,
        addressNumber = this@toOrganizationEntity.addressNumber,
        addressComplement = this@toOrganizationEntity.addressComplement,
        addressNeighborhood = this@toOrganizationEntity.addressNeighborhood,
        addressCity = this@toOrganizationEntity.addressCity,
        addressState = this@toOrganizationEntity.addressState,
        isActive = this@toOrganizationEntity.isActive,
    )
}

suspend fun OrganizationEntity.updateFromRequest(request: OrganizationUpsertRequest) = coroutineScope {
    this@updateFromRequest.copy(
        organizationName = request.organizationName,
        fantasyName = request.fantasyName,
        registrationNumber = request.registrationNumber,
        maximumUsers = request.maximumUsers,
        mainEmail = request.mainEmail,
        mainPhone = request.mainPhone,
        addressZipcode = request.addressZipcode,
        addressStreet = request.addressStreet,
        addressNumber = request.addressNumber,
        addressComplement = request.addressComplement,
        addressNeighborhood = request.addressNeighborhood,
        addressCity = request.addressCity,
        addressState = request.addressState,
        isActive = request.isActive,
        updatedAt = now(UTC)
    )
}

suspend fun OrganizationEntity.toOrganizationManagementEvent(type: ManagementEventType) = coroutineScope {
    OrganizationManagementEvent(
        eventType = type,
        eventTimestamp = now(UTC),
        eventKey = this@toOrganizationManagementEvent.id,
        eventData = this@toOrganizationManagementEvent.toOrganizationDataEvent(),
    )
}

suspend fun OrganizationEntity.toOrganizationDataEvent() = coroutineScope {
    OrganizationDataEvent(
        id = this@toOrganizationDataEvent.id,
        organizationName = this@toOrganizationDataEvent.organizationName,
        fantasyName = this@toOrganizationDataEvent.fantasyName,
        registrationNumber = this@toOrganizationDataEvent.registrationNumber,
        maximumUsers = this@toOrganizationDataEvent.maximumUsers,
        maximumUnits = this@toOrganizationDataEvent.maximumUnits,
        mainEmail = this@toOrganizationDataEvent.mainEmail,
        mainPhone = this@toOrganizationDataEvent.mainPhone,
        addressZipcode = this@toOrganizationDataEvent.addressZipcode,
        addressStreet = this@toOrganizationDataEvent.addressStreet,
        addressNumber = this@toOrganizationDataEvent.addressNumber,
        addressComplement = this@toOrganizationDataEvent.addressComplement,
        addressNeighborhood = this@toOrganizationDataEvent.addressNeighborhood,
        addressCity = this@toOrganizationDataEvent.addressCity,
        addressState = this@toOrganizationDataEvent.addressState.toAddressStateEvent(),
        isActive = this@toOrganizationDataEvent.isActive,
        createdAt = this@toOrganizationDataEvent.createdAt,
        updatedAt = this@toOrganizationDataEvent.updatedAt,
    )
}

