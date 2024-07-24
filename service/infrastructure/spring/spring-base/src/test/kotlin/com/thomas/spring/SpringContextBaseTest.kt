package com.thomas.spring

import java.time.Duration
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

@Suppress("UtilityClassWithPublicConstructor")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration
abstract class SpringContextBaseTest {

    companion object {
        protected const val DATABASE: String = "mongo_database"
        protected const val USERNAME: String = "mongo_username"
        protected const val PASSWORD: String = "mongo_password"
        protected const val PORT: Int = 27017

        @JvmStatic
        protected val MONGO_CONTAINER = GenericContainer("mongo:6.0.1")
            .withEnv("MONGO_INITDB_DATABASE", DATABASE)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", USERNAME)
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", PASSWORD)
            .withExposedPorts(PORT)

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            MONGO_CONTAINER.start()
            await atMost Duration.ofSeconds(30) until { MONGO_CONTAINER.isRunning }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            MONGO_CONTAINER.stop()
            await atMost Duration.ofSeconds(30) until { !MONGO_CONTAINER.isRunning }
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("security.database.connectionUrl") { "mongodb://${MONGO_CONTAINER.host}:${MONGO_CONTAINER.getMappedPort(PORT)}" }
            registry.add("security.database.databaseName") { DATABASE }
            registry.add("security.database.connectionUsername") { USERNAME }
            registry.add("security.database.connectionPassword") { PASSWORD }
            registry.add("security.database.apiVersion") { "V1" }
        }

    }

}
