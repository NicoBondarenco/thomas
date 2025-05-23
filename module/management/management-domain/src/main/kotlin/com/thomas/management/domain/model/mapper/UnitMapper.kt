package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.ApplicationEventType
import com.thomas.contract.messaging.ApplicationEventType.DELETE
import com.thomas.contract.messaging.management.unit.UnitDataEvent
import com.thomas.contract.messaging.management.unit.UnitManagementEvent
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.RoleGroupResponse
import com.thomas.management.domain.model.response.UnitResponse
import com.thomas.management.domain.model.response.UnitRoleResponse
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

suspend fun UnitEntity.toUnitManagementEvent(type: ApplicationEventType) = coroutineScope {
    UnitManagementEvent(
        eventType = type,
        eventTimestamp = now(UTC),
        eventKey = this@toUnitManagementEvent.id,
        eventData = this@toUnitManagementEvent.toUnitDataEvent(),
    )
}

suspend fun UnitEntity.toUnitDataEvent() = coroutineScope {
    UnitDataEvent(
        id = this@toUnitDataEvent.id,
        unitName = this@toUnitDataEvent.unitName,
        fantasyName = this@toUnitDataEvent.fantasyName,
        documentNumber = this@toUnitDataEvent.documentNumber,
        unitType = this@toUnitDataEvent.unitType.toUnitTypeEvent(),
        unitOrganization = this@toUnitDataEvent.unitOrganization.id,
        mainEmail = this@toUnitDataEvent.mainEmail,
        mainPhone = this@toUnitDataEvent.mainPhone,
        addressZipcode = this@toUnitDataEvent.addressZipcode,
        addressStreet = this@toUnitDataEvent.addressStreet,
        addressNumber = this@toUnitDataEvent.addressNumber,
        addressComplement = this@toUnitDataEvent.addressComplement,
        addressNeighborhood = this@toUnitDataEvent.addressNeighborhood,
        addressCity = this@toUnitDataEvent.addressCity,
        addressState = this@toUnitDataEvent.addressState.toAddressStateEvent(),
        isActive = this@toUnitDataEvent.isActive,
        createdAt = this@toUnitDataEvent.createdAt,
        updatedAt = this@toUnitDataEvent.updatedAt,
    )
}

suspend fun UUID.toUnitManagementEvent() = coroutineScope {
    UnitManagementEvent(
        eventType = DELETE,
        eventTimestamp = now(UTC),
        eventKey = this@toUnitManagementEvent,
        eventData = null,
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

suspend fun Collection<UnitEntity>.toUnitRoleResponses(
    unitRoles: Set<RoleGroupResponse>
): Set<UnitRoleResponse> = coroutineScope {
    this@toUnitRoleResponses.map {
        UnitRoleResponse(
            id = it.id,
            unitName = it.unitName,
            fantasyName = it.fantasyName,
            unitRoles = unitRoles,
        )
    }.toSet()
}
