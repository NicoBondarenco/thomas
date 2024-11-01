package com.thomas.management.domain.model.mapper

import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.UnitResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.coroutineScope

suspend fun UnitEntity.toUnitResponse() = coroutineScope {
    UnitResponse(
        id = id,
        unitName = unitName,
        fantasyName = fantasyName,
        documentNumber = documentNumber,
        unitType = unitType,
        unitOrganization = unitOrganization.toOrganizationResponse(),
        mainEmail = mainEmail,
        mainPhone = mainPhone,
        addressZipcode = addressZipcode,
        addressStreet = addressStreet,
        addressNumber = addressNumber,
        addressComplement = addressComplement,
        addressNeighborhood = addressNeighborhood,
        addressCity = addressCity,
        addressState = addressState,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

suspend fun UnitUpsertRequest.toUnitEntity(
    organizationEntity: OrganizationEntity,
) = coroutineScope {
    UnitEntity(
        unitName = this@toUnitEntity.unitName,
        fantasyName = this@toUnitEntity.fantasyName,
        documentNumber = this@toUnitEntity.documentNumber,
        unitType = this@toUnitEntity.unitType,
        unitOrganization = organizationEntity,
        mainEmail = this@toUnitEntity.mainEmail,
        mainPhone = this@toUnitEntity.mainPhone,
        addressZipcode = this@toUnitEntity.addressZipcode,
        addressStreet = this@toUnitEntity.addressStreet,
        addressNumber = this@toUnitEntity.addressNumber,
        addressComplement = this@toUnitEntity.addressComplement,
        addressNeighborhood = this@toUnitEntity.addressNeighborhood,
        addressCity = this@toUnitEntity.addressCity,
        addressState = this@toUnitEntity.addressState,
        isActive = this@toUnitEntity.isActive,
    )
}

suspend fun UnitEntity.updateFromRequest(request: UnitUpsertRequest) = coroutineScope {
    this@updateFromRequest.copy(
        unitName = request.unitName,
        fantasyName = request.fantasyName,
        documentNumber = request.documentNumber,
        unitType = request.unitType,
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
