package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.domain.model.request.SignupOrganizationRequest

internal val organizationEntity: OrganizationEntity
    get() = OrganizationEntity(
        organizationName = randomString(),
        registrationNumber = randomRegistrationNumber(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        maximumUsers = 10,
        maximumUnits = 10,
        addressZipcode = randomZipcode(),
        addressStreet = randomString(),
        addressNumber = randomString(),
        addressNeighborhood = randomString(),
        addressCity = randomString(numbers = false),
        addressState = AddressState.entries.random(),
    )

internal val signupOrganizationRequest: SignupOrganizationRequest
    get() = SignupOrganizationRequest(
        organizationName = randomString(),
        registrationNumber = randomRegistrationNumber(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        maximumUsers = 10,
        maximumUnits = 10,
        addressZipcode = randomZipcode(),
        addressStreet = randomString(),
        addressNumber = randomString(),
        addressNeighborhood = randomString(),
        addressCity = randomString(numbers = false),
        addressState = AddressState.entries.random(),
    )
