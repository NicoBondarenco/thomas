package com.thomas.exposed

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.general.UserProfile.ADMINISTRATOR
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityUser
import com.thomas.exposed.configuration.AUDITORY_SCHEMA_NAME
import com.thomas.exposed.configuration.AUDITORY_TABLE_SUFFIX
import com.thomas.exposed.configuration.ExposedObjectMapperFactory
import com.thomas.exposed.metadata.AuditoryTable
import com.thomas.exposed.metadata.TableMetadata
import com.thomas.exposed.model.auditable.AuditableEntity
import com.thomas.exposed.model.auditable.createAuditableEntity
import com.thomas.exposed.model.auditable.createChildEntity
import com.thomas.exposed.model.auditable.createNonAuditableEntity
import com.thomas.exposed.model.auditable.createParentEntity
import com.thomas.exposed.model.simple.EnumerationEntity
import com.thomas.exposed.model.simple.EnumerationValue
import com.thomas.exposed.model.simple.createEnumerationEntity
import com.thomas.exposed.properties.ExposedAuditoryProperties
import com.thomas.exposed.properties.ExposedDatabaseDialect
import com.thomas.exposed.properties.ExposedDatabaseProperties
import com.thomas.exposed.repository.SearchableRepository
import com.thomas.exposed.util.valuesJson
import java.util.UUID
import kotlin.random.Random.Default.nextInt
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.flushCache
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(PER_CLASS)
abstract class ExposedTest {

    companion object {

        @JvmStatic
        fun excludedTablesArgs(): List<Arguments> = listOf(
            Arguments.of("non_auditable")
        )

        @JvmStatic
        fun auditedTablesArgs(): List<Arguments> = listOf(
            Arguments.of("auditable")
        )

        @JvmStatic
        fun simpleTablesArgs(): List<Arguments> = listOf(
            Arguments.of("enumerated"),
            Arguments.of("simple_model"),
        )

    }

    abstract val databaseName: String
    abstract val driverClass: String
    abstract val databaseUsername: String
    abstract val databasePassword: String
    abstract val defaultSchema: String
    abstract val explicitDialect: ExposedDatabaseDialect

    private val auditoryProperties = ExposedAuditoryProperties(
        tablesPackage = "com.thomas.exposed.model.auditable",
        tablesExcluded = listOf(
            "common.non_auditable"
        ),
    )

    @Suppress("LeakingThis")
    protected fun auditableProperties(
        connectionUrl: String,
    ) = databaseProperties(connectionUrl, auditoryProperties)

    @Suppress("LeakingThis")
    protected fun simpleProperties(
        connectionUrl: String,
    ) = databaseProperties(connectionUrl, null)

    private fun databaseProperties(
        connectionUrl: String,
        auditoryProperties: ExposedAuditoryProperties?,
    ) = ExposedDatabaseProperties(
        driverClass = driverClass,
        connectionUrl = connectionUrl,
        databaseUsername = databaseUsername,
        databasePassword = databasePassword,
        defaultSchema = defaultSchema,
        explicitDialect = explicitDialect,
        maximumPool = 1,
        databaseAuditory = auditoryProperties,
    )

    private val securityUser = SecurityUser(
        userId = UUID.randomUUID(),
        firstName = "User",
        lastName = "Security",
        mainEmail = "user@example.com",
        userProfile = ADMINISTRATOR,
        isActive = true,
    ).apply { currentUser = this }

    private val mapper = ExposedObjectMapperFactory.create()

    protected lateinit var auditableDatabase: Database
    private lateinit var auditableTablesMetadata: List<TableMetadata>

    protected lateinit var simpleDatabase: Database
    private lateinit var simpleTablesMetadata: List<TableMetadata>

    private lateinit var searchableRepository: SearchableRepository

    abstract fun auditableDatabase(): Database

    abstract fun simpleDatabase(): Database

    abstract fun findTablesMetadata(): List<TableMetadata>

    open fun runAfterAll() {}

    @BeforeAll
    fun beforeAll() {
        auditableDatabase = auditableDatabase()
        auditableTablesMetadata = transaction(auditableDatabase) {
            findTablesMetadata()
        }
        simpleDatabase = simpleDatabase()
        simpleTablesMetadata = transaction(simpleDatabase) {
            findTablesMetadata()
        }
        searchableRepository = SearchableRepository(auditableDatabase)
    }

    @AfterAll
    fun afterAll() {
        runAfterAll()
    }

    @ParameterizedTest
    @MethodSource("excludedTablesArgs")
    fun `WHEN auditory is enabled THEN tables excluded should not have auditory tables created`(tableName: String) {
        assertNull(
            auditableTablesMetadata.firstOrNull {
                it.schema == AUDITORY_SCHEMA_NAME && it.table == "$tableName$AUDITORY_TABLE_SUFFIX"
            }
        )
    }

    @ParameterizedTest
    @MethodSource("auditedTablesArgs")
    fun `WHEN auditory is enabled THEN tables not excluded should have auditory tables created`(tableName: String) {
        assertNotNull(
            auditableTablesMetadata.firstOrNull {
                it.schema == AUDITORY_SCHEMA_NAME && it.table == "$tableName$AUDITORY_TABLE_SUFFIX"
            }
        )
    }

    @Test
    fun `WHEN save an audited entity SHOULD save auditory data on the respective table`() {
        val auditoryTable = object : AuditoryTable("auditable$AUDITORY_TABLE_SUFFIX") {}

        val entityId: UUID = UUID.randomUUID()
        var data = ""

        transaction(auditableDatabase) {
            createAuditableEntity(entityId)
            flushCache()
            data = AuditableEntity.findById(entityId)!!.valuesJson(mapper)
        }

        transaction(auditableDatabase) {
            auditoryTable.selectAll().where {
                auditoryTable.entityId eq entityId.toString()
            }.firstOrNull()?.apply {
                assertEquals(securityUser.userId, this[auditoryTable.userId])
                assertEquals(EntityChangeType.Created.name.uppercase(), this[auditoryTable.actionType])
                JSONAssert.assertEquals(data, this[auditoryTable.entitySnapshot].toString(), true)
            }
        }

    }

    @Test
    fun `WHEN saving a entity of a excluded table THEN should not throws any exception`() {
        assertDoesNotThrow {
            transaction(auditableDatabase) {
                createNonAuditableEntity()
            }
        }
    }

    @Test
    fun `WHEN update an audited entity SHOULD save auditory data on the respective table`() {
        val auditoryTable = object : AuditoryTable("auditable$AUDITORY_TABLE_SUFFIX") {}

        val entityId: UUID = UUID.randomUUID()
        var data = ""

        transaction(auditableDatabase) {
            val entity = createAuditableEntity(entityId)
            flushCache()
            entity.intValue = nextInt(100, 200)
            flushCache()
            data = entity.valuesJson(mapper)
        }

        transaction(auditableDatabase) {
            auditoryTable.selectAll().where {
                auditoryTable.entityId eq entityId.toString()
            }.maxByOrNull { it[auditoryTable.createdAt] }?.apply {
                assertEquals(securityUser.userId, this[auditoryTable.userId])
                assertEquals(EntityChangeType.Updated.name.uppercase(), this[auditoryTable.actionType])
                JSONAssert.assertEquals(data, this[auditoryTable.entitySnapshot].toString(), true)
            }
        }

    }

    @Test
    fun `WHEN delete an audited entity SHOULD save auditory data on the respective table`() {
        val auditoryTable = object : AuditoryTable("auditable$AUDITORY_TABLE_SUFFIX") {}

        val entityId: UUID = UUID.randomUUID()
        var data = ""

        transaction(auditableDatabase) {
            val entity = createAuditableEntity(entityId)
            flushCache()
            data = entity.valuesJson(mapper)
            entity.delete()
            flushCache()
        }

        transaction(auditableDatabase) {
            auditoryTable.selectAll().where {
                auditoryTable.entityId eq entityId.toString()
            }.maxByOrNull { it[auditoryTable.createdAt] }?.apply {
                assertEquals(securityUser.userId, this[auditoryTable.userId])
                assertEquals(EntityChangeType.Removed.name.uppercase(), this[auditoryTable.actionType])
                JSONAssert.assertEquals(data, this[auditoryTable.entitySnapshot].toString(), true)
            }
        }

    }

    @Test
    fun `WHEN saving a entity with FK THEN should audit the FK`() {
        val auditoryTable = object : AuditoryTable("child$AUDITORY_TABLE_SUFFIX") {}

        val entityId: UUID = UUID.randomUUID()
        var data = ""

        transaction(auditableDatabase) {
            val parent = createParentEntity()
            val child = createChildEntity(entityId, parent)
            flushCache()
            data = child.valuesJson(mapper)
        }

        transaction(auditableDatabase) {
            auditoryTable.selectAll().where {
                auditoryTable.entityId eq entityId.toString()
            }.firstOrNull()?.apply {
                assertEquals(securityUser.userId, this[auditoryTable.userId])
                JSONAssert.assertEquals(data, this[auditoryTable.entitySnapshot].toString(), true)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("simpleTablesArgs")
    fun `WHEN database is not auditable THEN should not create any tables`(tableName: String) {
        assertNull(
            simpleTablesMetadata.firstOrNull {
                it.schema == AUDITORY_SCHEMA_NAME && it.table == "$tableName$AUDITORY_TABLE_SUFFIX"
            }
        )
    }

    @Test
    fun `WHEN saving a entity on non auditable database THEN should not throws any exception`() {
        assertDoesNotThrow {
            transaction(simpleDatabase) {
                createNonAuditableEntity()
            }
        }
    }

    @Test
    fun `WHEN saving a entity on non auditable database THEN should save on auditory table`() {
        val entityId: UUID = UUID.randomUUID()

        assertDoesNotThrow {
            transaction(simpleDatabase) {
                createAuditableEntity(entityId)
            }
        }

    }

    @ParameterizedTest
    @EnumSource(EnumerationValue::class)
    fun `WHEN saving a non nullable enum THEN should map the enum correctly`(value: EnumerationValue) {
        val entityId = UUID.randomUUID()
        transaction(simpleDatabase) {
            createEnumerationEntity(
                id = entityId,
                valueEnum = value
            )
        }

        transaction(simpleDatabase) {
            assertEquals(value, EnumerationEntity.findById(entityId)!!.valueEnum)
        }
    }

    @Test
    fun `WHEN saving a nullable enum with null value THEN should map the enum correctly`() {
        val entityId = UUID.randomUUID()
        transaction(simpleDatabase) {
            createEnumerationEntity(
                id = entityId,
                nullableEnum = null
            )
        }

        transaction(simpleDatabase) {
            assertNull(EnumerationEntity.findById(entityId)!!.nullableEnum)
        }
    }

    @ParameterizedTest
    @EnumSource(EnumerationValue::class)
    fun `WHEN saving a nullable enum with non null value THEN should map the enum correctly`(value: EnumerationValue) {
        val entityId = UUID.randomUUID()
        transaction(simpleDatabase) {
            createEnumerationEntity(
                id = entityId,
                nullableEnum = value
            )
        }

        transaction(simpleDatabase) {
            assertEquals(value, EnumerationEntity.findById(entityId)!!.nullableEnum)
        }
    }

    @Test
    fun `WHEN select a list without conditions THEN should return full list`() {
        val result = searchableRepository.searchList()
        assertEquals(25, result.size)
    }

    @Test
    fun `WHEN select a list with conditions THEN should return filtered list`() {
        val result = searchableRepository.searchList("jose", 15)
        assertEquals(7, result.size)
        listOf(
            "Carla Joseana Medeiros",
            "Carlos José Alceu",
            "Joseandro Faria Lima",
            "Joseane Dalva Galote",
            "Josefa Alma Valença",
            "Joselias Espinoza Lovato",
            "Joseph Manoel Frias",
        ).forEach { name ->
            assertTrue(result.any { it.stringValue == name })
        }
    }

    @Test
    fun `WHEN select a page without conditions THEN should return full page`() {
        val result = searchableRepository.searchPage(
            PageRequest(
                3,
                5,
                listOf(PageSort("int_value", ASC))
            )
        )
        assertEquals(25, result.totalItems)
        assertEquals(5, result.totalPages)
        assertEquals(5, result.contentList.size)
        assertEquals(11, result.contentList[0].intValue)
        assertEquals(12, result.contentList[1].intValue)
        assertEquals(13, result.contentList[2].intValue)
        assertEquals(14, result.contentList[3].intValue)
        assertEquals(15, result.contentList[4].intValue)
    }

    @Test
    fun `WHEN select a page with conditions THEN should return filtered page`() {
        val result = searchableRepository.searchPage(
            "jose",
            15,
            PageRequest(
                2,
                4,
                listOf(PageSort("int_value", DESC))
            )
        )
        assertEquals(7, result.totalItems)
        assertEquals(2, result.totalPages)
        assertEquals(3, result.contentList.size)
        listOf(
            "Joseandro Faria Lima",
            "Carlos José Alceu",
            "Carla Joseana Medeiros",
        ).forEach { name ->
            assertTrue(result.contentList.any { it.stringValue == name })
        }
    }

    @Test
    fun `WHEN select a page with invalid sort column THEN should return unsorted page`() {
        val result = searchableRepository.searchPage(
            PageRequest(
                3,
                5,
                listOf(PageSort("another_field", ASC))
            )
        )
        assertEquals(25, result.totalItems)
        assertEquals(5, result.totalPages)
        assertEquals(5, result.contentList.size)
    }

}
