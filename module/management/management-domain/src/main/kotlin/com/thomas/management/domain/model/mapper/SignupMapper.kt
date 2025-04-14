package com.thomas.management.domain.model.mapper

import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.model.response.SignupResponse
import com.thomas.management.domain.properties.SignupProperties
import kotlinx.coroutines.coroutineScope

suspend fun SignupRequest.toSignupEntity(
    hasher: Hasher,
    signupProperties: SignupProperties
): SignupEntity = coroutineScope {
    organizationData.toOrganizationEntity(
        maxUnits = signupProperties.maxUnits,
        maxUsers = signupProperties.maxUsers,
    ).let {
        SignupEntity(
            organizationData = it,
            userData = userData.toUserEntity(it, hasher, signupProperties.defaultRoles),
        )
    }
}

suspend fun SignupEntity.toSignupResponse() = coroutineScope {
    SignupResponse(
        organizationData = this@toSignupResponse.organizationData.toOrganizationResponse(),
        userData = this@toSignupResponse.userData.toSignupUserResponse(),
    )
}
