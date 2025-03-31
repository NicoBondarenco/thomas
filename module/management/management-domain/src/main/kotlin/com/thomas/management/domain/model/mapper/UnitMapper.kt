package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.unit.UnitCreatedEvent
import com.thomas.contract.messaging.management.unit.UnitDeletedEvent
import com.thomas.contract.messaging.management.unit.UnitUpdatedEvent
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.UnitResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
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

suspend fun UnitEntity.toUnitCreatedEvent() = coroutineScope {
    UnitCreatedEvent(
        id = this@toUnitCreatedEvent.id,
        unitName = this@toUnitCreatedEvent.unitName,
        fantasyName = this@toUnitCreatedEvent.fantasyName,
        documentNumber = this@toUnitCreatedEvent.documentNumber,
        unitType = this@toUnitCreatedEvent.unitType.toUnitTypeEvent(),
        unitOrganization = this@toUnitCreatedEvent.unitOrganization.id,
        mainEmail = this@toUnitCreatedEvent.mainEmail,
        mainPhone = this@toUnitCreatedEvent.mainPhone,
        addressZipcode = this@toUnitCreatedEvent.addressZipcode,
        addressStreet = this@toUnitCreatedEvent.addressStreet,
        addressNumber = this@toUnitCreatedEvent.addressNumber,
        addressComplement = this@toUnitCreatedEvent.addressComplement,
        addressNeighborhood = this@toUnitCreatedEvent.addressNeighborhood,
        addressCity = this@toUnitCreatedEvent.addressCity,
        addressState = this@toUnitCreatedEvent.addressState.toAddressStateEvent(),
        isActive = this@toUnitCreatedEvent.isActive,
        createdAt = this@toUnitCreatedEvent.createdAt,
        updatedAt = this@toUnitCreatedEvent.updatedAt,
    )
}

suspend fun UnitEntity.toUnitUpdatedEvent() = coroutineScope {
    UnitUpdatedEvent(
        id = this@toUnitUpdatedEvent.id,
        unitName = this@toUnitUpdatedEvent.unitName,
        fantasyName = this@toUnitUpdatedEvent.fantasyName,
        documentNumber = this@toUnitUpdatedEvent.documentNumber,
        unitType = this@toUnitUpdatedEvent.unitType.toUnitTypeEvent(),
        unitOrganization = this@toUnitUpdatedEvent.unitOrganization.id,
        mainEmail = this@toUnitUpdatedEvent.mainEmail,
        mainPhone = this@toUnitUpdatedEvent.mainPhone,
        addressZipcode = this@toUnitUpdatedEvent.addressZipcode,
        addressStreet = this@toUnitUpdatedEvent.addressStreet,
        addressNumber = this@toUnitUpdatedEvent.addressNumber,
        addressComplement = this@toUnitUpdatedEvent.addressComplement,
        addressNeighborhood = this@toUnitUpdatedEvent.addressNeighborhood,
        addressCity = this@toUnitUpdatedEvent.addressCity,
        addressState = this@toUnitUpdatedEvent.addressState.toAddressStateEvent(),
        isActive = this@toUnitUpdatedEvent.isActive,
        createdAt = this@toUnitUpdatedEvent.createdAt,
        updatedAt = this@toUnitUpdatedEvent.updatedAt,
    )
}

suspend fun UUID.toUnitDeletedEvent() = coroutineScope {
    UnitDeletedEvent(
        id = this@toUnitDeletedEvent,
        deletedAt = now(UTC),
    )
}

suspend fun Map<UnitEntity, Set<SecurityUnitRole>>.toUnitRoleEntity(): Set<UnitRoleEntity> = coroutineScope {
    this@toUnitRoleEntity.map {
        UnitRoleEntity(
            roleUnit = it.key,
            roleList = it.value,
        )
    }.toSet()
}

