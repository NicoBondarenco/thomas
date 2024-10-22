package com.thomas.management.data.entity.generator

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.value.AddressState

object OrganizationGenerator {

    fun generateOrganizationEntity(): OrganizationEntity = OrganizationEntity(
        organizationName = randomString(),
        registrationNumber = randomRegistrationNumber(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        addressZipcode = randomZipcode(),
        addressStreet = randomString(),
        addressNumber = randomString(),
        addressNeighborhood = randomString(),
        addressCity = randomString(numbers = false),
        addressState = AddressState.entries.random(),
    )

}
