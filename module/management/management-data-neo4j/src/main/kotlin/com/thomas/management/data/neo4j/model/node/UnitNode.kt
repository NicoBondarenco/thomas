package com.thomas.management.data.neo4j.model.node

import com.thomas.database.neo4j.converter.UUIDConverter
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Relationship.Direction.OUTGOING
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "Unit")
data class UnitNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
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
    var updatedAt: ZonedDateTime

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnitNode

        if (id != other.id) return false
        if (unitName != other.unitName) return false
        if (fantasyName != other.fantasyName) return false
        if (documentNumber != other.documentNumber) return false
        if (unitType != other.unitType) return false
        if (unitOrganization.id != other.unitOrganization.id) return false
        if (mainEmail != other.mainEmail) return false
        if (mainPhone != other.mainPhone) return false
        if (addressZipcode != other.addressZipcode) return false
        if (addressStreet != other.addressStreet) return false
        if (addressNumber != other.addressNumber) return false
        if (addressComplement != other.addressComplement) return false
        if (addressNeighborhood != other.addressNeighborhood) return false
        if (addressCity != other.addressCity) return false
        if (addressState != other.addressState) return false
        if (isActive != other.isActive) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isActive.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + unitName.hashCode()
        result = 31 * result + (fantasyName?.hashCode() ?: 0)
        result = 31 * result + documentNumber.hashCode()
        result = 31 * result + unitType.hashCode()
        result = 31 * result + unitOrganization.id.hashCode()
        result = 31 * result + mainEmail.hashCode()
        result = 31 * result + mainPhone.hashCode()
        result = 31 * result + addressZipcode.hashCode()
        result = 31 * result + addressStreet.hashCode()
        result = 31 * result + addressNumber.hashCode()
        result = 31 * result + (addressComplement?.hashCode() ?: 0)
        result = 31 * result + addressNeighborhood.hashCode()
        result = 31 * result + addressCity.hashCode()
        result = 31 * result + addressState.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

}
