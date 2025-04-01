package com.thomas.management.data.neo4j.model.mapper

import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.neo4j.model.node.OrganizationNode

fun OrganizationEntity.toOrganizationNode() = OrganizationNode(
    id = this.id,
    organizationName = this.organizationName,
    fantasyName = this.fantasyName,
    registrationNumber = this.registrationNumber,
    maximumUsers = this.maximumUsers,
    maximumUnits = this.maximumUnits,
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

fun OrganizationNode.toOrganizationEntity() = OrganizationEntity(
    id = this.id,
    organizationName = this.organizationName,
    fantasyName = this.fantasyName,
    registrationNumber = this.registrationNumber,
    maximumUsers = this.maximumUsers,
    maximumUnits = this.maximumUnits,
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
