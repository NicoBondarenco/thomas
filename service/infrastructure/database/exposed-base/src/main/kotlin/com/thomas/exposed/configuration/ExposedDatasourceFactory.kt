package com.thomas.exposed.configuration

import com.thomas.exposed.properties.ExposedDatabaseProperties
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object ExposedDatasourceFactory {

    fun create(
        properties: ExposedDatabaseProperties
    ): DataSource = HikariDataSource(hikariConfiguration(properties))

    private fun hikariConfiguration(
        properties: ExposedDatabaseProperties
    ) = HikariConfig().apply {
        driverClassName = properties.driverClass
        jdbcUrl = properties.connectionUrl
        username = properties.databaseUsername
        password = properties.databasePassword
        idleTimeout = properties.idleTimeout
        connectionTimeout = properties.connectionTimeout
        maximumPoolSize = properties.maximumPool
        maxLifetime = properties.maximumLifetime
    }

}
