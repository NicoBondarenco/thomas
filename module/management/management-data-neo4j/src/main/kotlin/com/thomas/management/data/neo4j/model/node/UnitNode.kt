package com.thomas.management.data.neo4j.model.node

import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import java.time.ZonedDateTime
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Relationship.Direction.OUTGOING

@NoArgsConstructor
@NodeEntity(value = "Unit")
data class UnitNode(
    @Id
    @Property(name = "id")
    override var id: UUID,

    @Property(name = "unit_name")
    var unitName: String,

    @Property(name = "fantasy_name")
    var fantasyName: String?,

    @Property(name = "document_number")
    var documentNumber: String,

    @Property(name = "unit_type")
    var unitType: UnitType,

    @Relationship(type = "UNIT_BELONGS_TO_ORGANIZATION", direction = OUTGOING)
    var unitOrganization: OrganizationNode,

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
    var updatedAt: ZonedDateTime,
) : Neo4JNode
