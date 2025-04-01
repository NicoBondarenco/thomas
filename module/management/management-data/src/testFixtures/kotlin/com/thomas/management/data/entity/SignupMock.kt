package com.thomas.management.data.entity

val signupEntity: SignupEntity
    get() = userEntity.let {
        SignupEntity(
            userData = it,
            organizationData = it.userOrganization,
        )
    }
