package com.thomas.management.domain.model.mapper

import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.domain.model.request.SignupOrganizationRequest
import com.thomas.management.domain.model.response.SignupOrganizationResponse
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

suspend fun OrganizationEntity.toSignupOrganizationResponse() = coroutineScope {
    SignupOrganizationResponse(
        id = this@toSignupOrganizationResponse.id,
        organizationName = this@toSignupOrganizationResponse.organizationName,
        fantasyName = this@toSignupOrganizationResponse.fantasyName,
        registrationNumber = this@toSignupOrganizationResponse.registrationNumber,
        maximumUsers = this@toSignupOrganizationResponse.maximumUsers,
        mainEmail = this@toSignupOrganizationResponse.mainEmail,
        mainPhone = this@toSignupOrganizationResponse.mainPhone,
        addressZipcode = this@toSignupOrganizationResponse.addressZipcode,
        addressStreet = this@toSignupOrganizationResponse.addressStreet,
        addressNumber = this@toSignupOrganizationResponse.addressNumber,
        addressComplement = this@toSignupOrganizationResponse.addressComplement,
        addressNeighborhood = this@toSignupOrganizationResponse.addressNeighborhood,
        addressCity = this@toSignupOrganizationResponse.addressCity,
        addressState = this@toSignupOrganizationResponse.addressState,
        isActive = this@toSignupOrganizationResponse.isActive,
        createdAt = this@toSignupOrganizationResponse.createdAt,
        updatedAt = this@toSignupOrganizationResponse.updatedAt,
    )
}
