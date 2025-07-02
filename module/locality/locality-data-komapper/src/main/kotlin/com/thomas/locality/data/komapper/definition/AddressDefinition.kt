package com.thomas.locality.data.komapper.definition

import com.thomas.core.model.general.AddressState
import com.thomas.locality.data.entity.AddressEntity
import java.time.OffsetDateTime
import java.util.UUID
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntityDef(entity = AddressEntity::class, aliases = ["address"])
@KomapperTable("address", schema = "locality", alwaysQuote = true)
data class AddressDefinition(

    @KomapperId
    @KomapperColumn(name = "id", alwaysQuote = true)
    val id: UUID,

    @KomapperColumn(name = "zipcode_number", alwaysQuote = true)
    val zipcodeNumber: String,

    @KomapperColumn(name = "address_street", alwaysQuote = true)
    val addressStreet: String?,

    @KomapperColumn(name = "address_complement", alwaysQuote = true)
    val addressComplement: String?,

    @KomapperColumn(name = "address_unit", alwaysQuote = true)
    val addressUnit: String?,

    @KomapperColumn(name = "address_neighborhood", alwaysQuote = true)
    val addressNeighborhood: String?,

    @KomapperColumn(name = "address_city", alwaysQuote = true)
    val addressCity: String,

    @KomapperColumn(name = "address_state", alwaysQuote = true)
    val addressState: AddressState,

    @KomapperColumn(name = "city_code", alwaysQuote = true)
    val cityCode: String?,

    @KomapperColumn(name = "reference_code", alwaysQuote = true)
    val referenceCode: String?,

    @KomapperColumn(name = "phone_code", alwaysQuote = true)
    val phoneCode: String?,

    @KomapperColumn(name = "federal_code", alwaysQuote = true)
    val federalCode: String?,

    @KomapperColumn(name = "created_at", alwaysQuote = true)
    val createdAt: OffsetDateTime,

    @KomapperColumn(name = "updated_at", alwaysQuote = true)
    val updatedAt: OffsetDateTime,

)
