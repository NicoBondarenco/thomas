package com.thomas.management.spring.configuration

import com.thomas.management.spring.configuration.properties.Neo4JProperties
import org.neo4j.ogm.session.SessionFactory
import org.springframework.boot.actuate.neo4j.Neo4jReactiveHealthIndicator
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.neo4j.ogm.config.Configuration as Neo4JConfiguration
import org.neo4j.ogm.driver.Driver
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver

@Configuration
@EnableConfigurationProperties(Neo4JProperties::class)
class Neo4JConfiguration {

    @Bean
    fun neo4jConfiguration(
        neo4JProperties: Neo4JProperties,
    ): Neo4JConfiguration = Neo4JConfiguration.Builder()
        .uri(neo4JProperties.connectionUri)
        .database(neo4JProperties.databaseName)
        .credentials(neo4JProperties.databaseUsername, neo4JProperties.databasePassword)
        .connectionLivenessCheckTimeout(neo4JProperties.livenessCheck)
        .verifyConnection(neo4JProperties.verifyConnection)
        .connectionPoolSize(neo4JProperties.poolSize)
        .useNativeTypes()
        .build()

    @Bean
    fun sessionFactory(
        configuration: Neo4JConfiguration,
        neo4JProperties: Neo4JProperties,
    ): SessionFactory = SessionFactory(
        configuration,
        *neo4JProperties.nodePackages.toTypedArray(),
    )

}
