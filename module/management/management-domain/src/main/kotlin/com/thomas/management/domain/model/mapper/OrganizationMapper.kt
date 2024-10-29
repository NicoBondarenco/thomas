package com.thomas.management.domain.model.mapper

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
