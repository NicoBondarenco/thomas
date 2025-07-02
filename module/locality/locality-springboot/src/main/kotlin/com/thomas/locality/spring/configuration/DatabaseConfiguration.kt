package com.thomas.locality.spring.configuration

import com.thomas.database.komapper.extension.EmptyR2dbcDataTypeProvider
import com.thomas.locality.spring.configuration.properties.DatabaseProperties
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import java.time.Duration
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDataTypeProvider
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDialect
import org.komapper.r2dbc.DefaultR2dbcDatabaseConfig
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfiguration {

    @Bean
    @Qualifier("connectionFactory")
    fun connectionFactory(
        properties: DatabaseProperties,
    ): ConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host(properties.host)
            .port(properties.port)
            .database(properties.database)
            .username(properties.username)
            .password(properties.password)
            .schema(properties.schema)
            .build()
    )

    @Bean
    @Qualifier("connectionPool")
    fun connectionPool(
        properties: DatabaseProperties,
        @Qualifier("connectionFactory") connection: ConnectionFactory,
    ): ConnectionFactory = ConnectionPool(
        ConnectionPoolConfiguration.builder(connection)
            .initialSize(properties.pool.initialSize)
            .maxSize(properties.pool.maxSize)
            .maxIdleTime(Duration.ofSeconds(properties.pool.idleTime))
            .maxCreateConnectionTime(Duration.ofSeconds(properties.pool.creationTimeout))
            .maxAcquireTime(Duration.ofSeconds(properties.pool.acquireTimeout))
            .maxLifeTime(Duration.ofSeconds(properties.pool.maxLifetime))
            .validationQuery(properties.pool.validationQuery)
            .build()
    )

    @Bean
    fun database(
        @Qualifier("connectionPool") connection: ConnectionFactory,
    ): R2dbcDatabase = R2dbcDatabase(
        DefaultR2dbcDatabaseConfig(
            connectionFactory = connection,
            dialect = PostgreSqlR2dbcDialect(),
            dataTypeProvider = PostgreSqlR2dbcDataTypeProvider(EmptyR2dbcDataTypeProvider)
        )
    )

}