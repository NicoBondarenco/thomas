package com.thomas.locality.spring.configuration

import com.thomas.locality.data.komapper.repository.AddressKomapperRepository
import com.thomas.locality.data.repository.AddressRepository
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration {

    @Bean
    fun addressRepository(
        database: R2dbcDatabase
    ): AddressRepository = AddressKomapperRepository(database)

}