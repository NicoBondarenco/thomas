package com.thomas.management.domain.model.mapper

import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.model.response.SignupResponse
import kotlinx.coroutines.coroutineScope

suspend fun SignupRequest.toSignupEntity(
    hasher: Hasher
): SignupEntity = coroutineScope {
    organizationData.toOrganizationEntity().let {
        SignupEntity(
            organizationData = it,
            userData = userData.toUserEntity(it, hasher),
        )
    }
}

suspend fun SignupEntity.toSignupResponse() = coroutineScope {
    SignupResponse(
        organizationData = this@toSignupResponse.organizationData.toOrganizationResponse(),
        userData = this@toSignupResponse.userData.toSignupUserResponse(),
    )
}
