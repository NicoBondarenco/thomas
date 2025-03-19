package com.thomas.management.data.neo4j.model.node

import com.thomas.database.neo4j.converter.UUIDConverter
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import com.thomas.management.data.entity.value.AddressState
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "Organization")
data class OrganizationNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "organization_name")
    var organizationName: String,

    @Property(name = "fantasy_name")
    var fantasyName: String?,

    @Property(name = "registration_number")
    var registrationNumber: String,

    @Property(name = "maximum_users")
    var maximumUsers: Int,

    @Property(name = "maximum_units")
    var maximumUnits: Int,

    @Property(name = "main_email")
    var mainEmail: String,

    @Property(name = "main_phone")
    var mainPhone: String,

    @Property(name = "address_zipcode")
    var addressZipcode: String,

    @Property(name = "address_street")
    var addressStreet: String,

    @Property(name = "address_number")
    var addressNumber: String,

    @Property(name = "address_complement")
    var addressComplement: String?,

    @Property(name = "address_neighborhood")
    var addressNeighborhood: String,

    @Property(name = "address_city")
    var addressCity: String,

    @Property(name = "address_state")
    var addressState: AddressState,

    @Property(name = "is_active")
    var isActive: Boolean,

    @Property(name = "created_at")
    var createdAt: ZonedDateTime,

    @Property(name = "updated_at")
    var updatedAt: ZonedDateTime

) : Neo4JNode<UUID>
