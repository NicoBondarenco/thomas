package com.thomas.management.data.neo4j.model.mapper

import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.neo4j.model.node.UnitNode

fun UnitEntity.toUnitNode() = UnitNode(
    id = this.id,
    unitName = this.unitName,
    fantasyName = this.fantasyName,
    documentNumber = this.documentNumber,
    unitType = this.unitType,
    unitOrganization = this.unitOrganization.toOrganizationNode(),
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    addressZipcode = this.addressZipcode,
    addressStreet = this.addressStreet,
    addressNumber = this.addressNumber,
    addressComplement = this.addressComplement,
    addressNeighborhood = this.addressNeighborhood,
    addressCity = this.addressCity,
    addressState = this.addressState,
    isActive = this.isActive,
    createdAt = this.createdAt.toZonedDateTime(),
    updatedAt = this.updatedAt.toZonedDateTime(),
)

fun UnitNode.toUnitEntity() = UnitEntity(
    id = this.id,
    unitName = this.unitName,
    fantasyName = this.fantasyName,
    documentNumber = this.documentNumber,
    unitType = this.unitType,
    unitOrganization = this.unitOrganization.toOrganizationEntity(),
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    addressZipcode = this.addressZipcode,
    addressStreet = this.addressStreet,
    addressNumber = this.addressNumber,
    addressComplement = this.addressComplement,
    addressNeighborhood = this.addressNeighborhood,
    addressCity = this.addressCity,
    addressState = this.addressState,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
)