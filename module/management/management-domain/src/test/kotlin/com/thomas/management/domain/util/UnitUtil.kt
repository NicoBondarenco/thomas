package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import com.thomas.management.data.entity.value.UnitType.NATURAL
import com.thomas.management.domain.model.request.UnitUpsertRequest

internal val unitEntity: UnitEntity
    get() = UnitType.entries.random().let {
        UnitEntity(
            unitName = if (it == NATURAL) randomString(numbers = false) else randomString(),
            fantasyName = listOf(null, randomString()).random(),
            documentNumber = if (it == NATURAL) randomDocumentNumber() else randomRegistrationNumber(),
            unitType = it,
            unitOrganization = organizationEntity,
            mainEmail = randomEmail(),
            mainPhone = randomPhone(),
            addressZipcode = randomZipcode(),
            addressStreet = randomString(),
            addressNumber = randomString(),
            addressComplement = listOf(null, randomString()).random(),
            addressNeighborhood = randomString(),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
            isActive = listOf(true, false).random(),
        )
    }

internal val unitUpsertRequest: UnitUpsertRequest
    get() = UnitType.entries.random().let {
        UnitUpsertRequest(
            unitName = if (it == NATURAL) randomString(numbers = false) else randomString(),
            fantasyName = listOf(null, randomString()).random(),
            documentNumber = if (it == NATURAL) randomDocumentNumber() else randomRegistrationNumber(),
            unitType = it,
            mainEmail = randomEmail(),
            mainPhone = randomPhone(),
            addressZipcode = randomZipcode(),
            addressStreet = randomString(),
            addressNumber = randomString(),
            addressComplement = listOf(null, randomString()).random(),
            addressNeighborhood = randomString(),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
            isActive = listOf(true, false).random(),
        )
    }
