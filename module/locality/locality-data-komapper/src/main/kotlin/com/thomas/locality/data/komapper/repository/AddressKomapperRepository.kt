package com.thomas.locality.data.komapper.repository

import com.thomas.database.komapper.repository.KomapperRepository
import com.thomas.locality.data.entity.AddressEntity
import com.thomas.locality.data.komapper.definition._AddressDefinition
import com.thomas.locality.data.komapper.definition.address
import com.thomas.locality.data.repository.AddressRepository
import java.util.UUID
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.r2dbc.R2dbcDatabase

class AddressKomapperRepository(
    database: R2dbcDatabase
): KomapperRepository<AddressEntity, UUID, _AddressDefinition>(
    database = database,
    metamodel = Meta.address
), AddressRepository {

    override suspend fun create(
        entity: AddressEntity
    ): AddressEntity = this.insert(entity)

    override suspend fun findByZipcode(
        zipcode: String
    ): AddressEntity? = database.runQuery(
        QueryDsl.from(metamodel).where { metamodel.zipcodeNumber eq zipcode }
    ).firstOrNull()

}
