package com.thomas.management.spring.configuration

import com.thomas.exposed.configuration.ExposedDatabaseFactory
import com.thomas.exposed.properties.ExposedDatabaseProperties
import com.thomas.management.spring.properties.ManagementProperties
import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ManagementProperties::class)
class DatabaseConfiguration {

    @Bean("management-database-properties")
    fun exposedDatabaseProperties(
        managementProperties: ManagementProperties,
    ): ExposedDatabaseProperties = managementProperties.database

    @Bean("management-database-datasource")
    fun dataSource(
        @Qualifier("management-database-properties") properties: ExposedDatabaseProperties,
    ): DataSource = DataSourceBuilder.create().also {
        it.driverClassName(properties.driverClass)
        it.url(properties.connectionUrl)
        it.username(properties.databaseUsername)
        it.password(properties.databasePassword)
    }.build()

    @Bean("management-database")
    fun database(
        @Qualifier("management-database-datasource") datasource: DataSource,
        @Qualifier("management-database-properties") properties: ExposedDatabaseProperties,
    ): Database = ExposedDatabaseFactory.create(
        datasource = datasource,
        properties = properties
    )

}
