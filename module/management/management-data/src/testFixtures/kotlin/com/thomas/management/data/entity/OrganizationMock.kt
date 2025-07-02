package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.core.model.general.AddressState

val organizationEntity: OrganizationEntity
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
