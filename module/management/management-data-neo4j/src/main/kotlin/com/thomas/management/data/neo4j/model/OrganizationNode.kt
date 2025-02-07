package com.thomas.management.data.neo4j.model

import com.thomas.management.data.entity.value.AddressState
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NodeEntity(value = "organization")
data class OrganizationNode(
    @Id
    @Property(name="name")
    val id: UUID,

    @Property(name="organization_name")
    val organizationName: String,

    @Property(name="fantasy_name")
    val fantasyName: String?,

    @Property(name="registration_number")
    val registrationNumber: String,

    @Property(name="maximum_users")
    val maximumUsers: Int,

    @Property(name="maximum_units")
    val maximumUnits: Int,

    @Property(name="main_email")
    val mainEmail: String,

    @Property(name="main_phone")
    val mainPhone: String,

    @Property(name="address_zipcode")
    val addressZipcode: String,

    @Property(name="address_street")
    val addressStreet: String,

    @Property(name="address_number")
    val addressNumber: String,

    @Property(name="address_complement")
    val addressComplement: String?,

    @Property(name="address_neighborhood")
    val addressNeighborhood: String,

    @Property(name="address_city")
    val addressCity: String,

    @Property(name="address_state")
    val addressState: AddressState,

    @Property(name="is_active")
    val isActive: Boolean,

    @Property(name="created_at")
    val createdAt: ZonedDateTime,

    @Property(name="updated_at")
    val updatedAt: ZonedDateTime,
)
