package com.thomas.database.komapper.repository

import com.thomas.core.model.general.Gender
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.core.util.NumberUtils.randomBigDecimal
import com.thomas.database.komapper.definition.entityTestEntity
import com.thomas.database.komapper.entity.EntityTestEntity
import com.thomas.database.komapper.extension.EmptyR2dbcDataTypeProvider
import com.thomas.database.komapper.extension.readTransaction
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.h2.H2ConnectionOption
import io.r2dbc.spi.ConnectionFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID
import kotlin.reflect.KType
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.dialect.h2.r2dbc.H2R2dbcDataTypeProvider
import org.komapper.dialect.h2.r2dbc.H2R2dbcDialect
import org.komapper.r2dbc.DefaultR2dbcDatabaseConfig
import org.komapper.r2dbc.R2dbcDataType
import org.komapper.r2dbc.R2dbcDataTypeProvider
import org.komapper.r2dbc.R2dbcDatabase

@TestInstance(PER_CLASS)
class KomapperRepositoryTest {

    private val databaseName: String = "test-database"
    private val databaseUsername: String = "thomas-test"
    private val databasePassword: String = "thomas-test"

    private val metadata = Meta.entityTestEntity

    private val script = KomapperRepositoryTest::class.java
        .getResource("/h2-scripts/database-data.sql")!!
        .readText()

    private lateinit var connection: ConnectionFactory
    private lateinit var database: R2dbcDatabase
    private lateinit var repository: EntityTestKomapperRepository
    private lateinit var entities: List<EntityTestEntity>

    @BeforeAll
    fun beforeAll() {
        connection = H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory(databaseName)
                .property(H2ConnectionOption.INIT, "RUNSCRIPT FROM 'classpath:h2-scripts/database-creation.sql'")
                .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                .username(databaseUsername)
                .password(databasePassword)
                .build()
        )
        database = R2dbcDatabase(
            DefaultR2dbcDatabaseConfig(
                connectionFactory = connection,
                dialect = H2R2dbcDialect(),
                dataTypeProvider = H2R2dbcDataTypeProvider(EmptyR2dbcDataTypeProvider)
            )
        )
        repository = EntityTestKomapperRepository(database)
    }

    @BeforeEach
    fun beforeEach() = runTest {
        database.runQuery(QueryDsl.executeScript(script))
        entities = database.readTransaction {
            database.runQuery(QueryDsl.from(metadata)).toList()
        }
    }

    @AfterEach
    fun afterEach() = runTest {
        database.runQuery(QueryDsl.delete(metadata).all())
    }

    @Test
    fun `should find entity by id when entity exists`() = runTest {
        val testEntity = EntityTestEntity()
        val insertedEntity = repository.insert(testEntity)

        val foundEntity = repository.id(insertedEntity.id)

        foundEntity.shouldNotBeNull()
        foundEntity.id shouldBe insertedEntity.id
        foundEntity.stringRequired shouldBe testEntity.stringRequired
        foundEntity.integerRequired shouldBe testEntity.integerRequired
    }

    @Test
    fun `should return null when finding entity by non-existing id`() = runTest {
        val nonExistingId = UUID.randomUUID()
        val foundEntity = repository.id(nonExistingId)
        foundEntity.shouldBeNull()
    }

    @Test
    fun `should find all entities when entities exist`() = runTest {
        val entity1 = EntityTestEntity(stringRequired = "Entity 1")
        val entity2 = EntityTestEntity(stringRequired = "Entity 2")
        val entity3 = EntityTestEntity(stringRequired = "Entity 3")

        repository.insert(entity1)
        repository.insert(entity2)
        repository.insert(entity3)

        val allEntities = repository.findAll()

        allEntities.shouldNotBeEmpty()
        allEntities.map { it.stringRequired } shouldContainAll listOf("Entity 1", "Entity 2", "Entity 3")
    }

    @Test
    fun `should insert entity successfully`() = runTest {
        val testEntity = EntityTestEntity()

        val insertedEntity = repository.insert(testEntity)

        insertedEntity.shouldNotBeNull()
        insertedEntity.stringRequired shouldBe testEntity.stringRequired
        insertedEntity.integerRequired shouldBe testEntity.integerRequired
        insertedEntity.bigdecimalRequired shouldBe testEntity.bigdecimalRequired
        insertedEntity.enumerationRequired shouldBe testEntity.enumerationRequired

        val foundEntity = repository.id(insertedEntity.id)
        foundEntity.shouldNotBeNull()
        foundEntity.id shouldBe insertedEntity.id
    }

    @Test
    fun `should insert entity with nullable fields`() = runTest {
        val gender = Gender.entries.random()
        val testEntity = EntityTestEntity(
            stringNullable = "Nullable String",
            integerNullable = 999,
            bigdecimalNullable = BigDecimal("123.456"),
            enumerationNullable = gender
        )

        val insertedEntity = repository.insert(testEntity)

        insertedEntity.shouldNotBeNull()
        insertedEntity.stringNullable shouldBe "Nullable String"
        insertedEntity.integerNullable shouldBe 999
        insertedEntity.bigdecimalNullable shouldBe BigDecimal("123.456")
        insertedEntity.enumerationNullable shouldBe gender
    }

    @Test
    fun `should update entity successfully`() = runTest {
        val originalEntity = EntityTestEntity(stringRequired = "Original")
        val insertedEntity = repository.insert(originalEntity)

        val updatedEntity = insertedEntity.copy(
            stringRequired = "Updated",
            integerRequired = 999,
            bigdecimalRequired = BigDecimal("999.99")
        )

        val result = repository.update(updatedEntity)

        result.shouldNotBeNull()
        result.id shouldBe insertedEntity.id
        result.stringRequired shouldBe "Updated"
        result.integerRequired shouldBe 999
        result.bigdecimalRequired shouldBe BigDecimal("999.99")

        val foundEntity = repository.id(insertedEntity.id)
        foundEntity.shouldNotBeNull()
        foundEntity.stringRequired shouldBe "Updated"
        foundEntity.integerRequired shouldBe 999
    }

    @Test
    fun `should update nullable fields to null`() = runTest {
        val originalEntity = EntityTestEntity(
            stringNullable = "Will be null",
            integerNullable = 123
        )
        val insertedEntity = repository.insert(originalEntity)

        val updatedEntity = insertedEntity.copy(
            stringNullable = null,
            integerNullable = null
        )

        val result = repository.update(updatedEntity)

        result.shouldNotBeNull()
        result.stringNullable.shouldBeNull()
        result.integerNullable.shouldBeNull()

        val foundEntity = repository.id(insertedEntity.id)
        foundEntity.shouldNotBeNull()
        foundEntity.stringNullable.shouldBeNull()
        foundEntity.integerNullable.shouldBeNull()
    }

    @Test
    fun `should delete entity by id successfully`() = runTest {
        val testEntity = EntityTestEntity()
        val insertedEntity = repository.insert(testEntity)

        repository.delete(insertedEntity.id)

        val foundEntity = repository.id(insertedEntity.id)
        foundEntity.shouldBeNull()
    }

    @Test
    fun `should handle delete of non-existing entity gracefully`() = runTest {
        val nonExistingId = UUID.randomUUID()

        repository.delete(nonExistingId)
    }

    @Test
    fun `should return paged results with default pagination`() = runTest {
        repeat(15) { index ->
            repository.insert(EntityTestEntity(stringRequired = "Entity $index"))
        }

        val pagedResult = repository.paged(stringValue = "Entity")

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 10
        pagedResult.totalItems shouldBe 15
        pagedResult.totalPages shouldBe 2
        pagedResult.pageNumber shouldBe 1
        pagedResult.pageSize shouldBe 10
    }

    @Test
    fun `should return paged results with custom page size`() = runTest {
        val ids = entities.shuffled().take(8).map { it.id }

        val pageRequest = PageRequest(pageNumber = 1, pageSize = 5)

        val pagedResult = repository.paged(entityIds = ids, pageable = pageRequest)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 5
        pagedResult.totalItems shouldBe 8
        pagedResult.totalPages shouldBe 2
        pagedResult.pageNumber shouldBe 1
        pagedResult.pageSize shouldBe 5
    }

    @Test
    fun `should return second page of paged results`() = runTest {
        val ids = entities.shuffled().take(15).map { it.id }

        val pageRequest = PageRequest(pageNumber = 2, pageSize = 10)

        val pagedResult = repository.paged(entityIds = ids, pageable = pageRequest)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 5
        pagedResult.totalItems shouldBe 15
        pagedResult.totalPages shouldBe 2
        pagedResult.pageNumber shouldBe 2
        pagedResult.pageSize shouldBe 10
    }

    @Test
    fun `should filter by string value in paged results`() = runTest {
        repository.insert(EntityTestEntity(stringRequired = "João Silva"))
        repository.insert(EntityTestEntity(stringRequired = "João Pereira"))

        val pagedResult = repository.paged(stringValue = "joão")

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 2
        pagedResult.totalItems shouldBe 2
        pagedResult.contentList.forEach { entity ->
            entity.stringRequired.lowercase() shouldContain "joão"
        }
    }

    @Test
    fun `should filter by integer value in paged results`() = runTest {
        repository.insert(EntityTestEntity(integerRequired = 100))
        repository.insert(EntityTestEntity(integerRequired = 100))

        val pagedResult = repository.paged(intValue = 100)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 2
        pagedResult.totalItems shouldBe 2
        pagedResult.contentList.forEach { entity ->
            entity.integerRequired shouldBe 100
        }
    }

    @Test
    fun `should filter by BigDecimal value in paged results`() = runTest {
        val searchValue = randomBigDecimal(10000.0, 100000.0).setScale(5, RoundingMode.HALF_UP)
        repository.insert(EntityTestEntity(bigdecimalRequired = searchValue))
        repository.insert(EntityTestEntity(bigdecimalRequired = searchValue))

        val pagedResult = repository.paged(bigdecimalValue = searchValue)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 2
        pagedResult.totalItems shouldBe 2
        pagedResult.contentList.forEach { entity ->
            entity.bigdecimalRequired shouldBe searchValue.setScale(5, RoundingMode.HALF_UP)
        }
    }

    @Test
    fun `should filter by enum value in paged results`() = runTest {
        Gender.entries.forEach { gender ->
            val total = entities.count { it.enumerationRequired == gender || it.enumerationNullable == gender }

            val pagedResult = repository.paged(enumValue = gender)

            pagedResult.shouldNotBeNull()
            pagedResult.contentList shouldHaveSize (total.takeIf { it <= 10 } ?: 10)
            pagedResult.totalItems shouldBe total
            pagedResult.contentList.forEach { entity ->
                listOf(entity.enumerationRequired, entity.enumerationNullable).contains(gender) shouldBe true
            }
        }
    }

    @Test
    fun `should filter by date range in paged results`() = runTest {
        val createdStart = entities.random().createdAt.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val createdEnd = entities.filter { it.createdAt > createdStart }
            .random()
            .createdAt.withHour(23).withMinute(59).withSecond(59).withNano(999999999)

        val total = entities.count { it.createdAt in createdStart..createdEnd }

        val pagedResult = repository.paged(
            createdStart = createdStart,
            createdEnd = createdEnd
        )

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize (total.takeIf { it <= 10 } ?: 10)
        pagedResult.totalItems shouldBe total
    }

    @Test
    fun `should apply sorting in paged results`() = runTest {

        val filtered = entities.sortedBy { it.stringRequired }.drop(10).take(10)

        val pageRequest = PageRequest(
            pageNumber = 2,
            pageSize = 10,
            pageSort = listOf(
                PageSort(sortField = "string_required", sortDirection = PageSortDirection.ASC)
            )
        )

        val pagedResult = repository.paged(pageable = pageRequest)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 10
        pagedResult.contentList[0].stringRequired shouldBe filtered[0].stringRequired
        pagedResult.contentList[1].stringRequired shouldBe filtered[1].stringRequired
        pagedResult.contentList[2].stringRequired shouldBe filtered[2].stringRequired
        pagedResult.contentList[3].stringRequired shouldBe filtered[3].stringRequired
        pagedResult.contentList[4].stringRequired shouldBe filtered[4].stringRequired
        pagedResult.contentList[5].stringRequired shouldBe filtered[5].stringRequired
        pagedResult.contentList[6].stringRequired shouldBe filtered[6].stringRequired
        pagedResult.contentList[7].stringRequired shouldBe filtered[7].stringRequired
        pagedResult.contentList[8].stringRequired shouldBe filtered[8].stringRequired
        pagedResult.contentList[9].stringRequired shouldBe filtered[9].stringRequired
    }

    @Test
    fun `should apply descending sorting in paged results`() = runTest {
        val filtered = entities.sortedByDescending { it.integerRequired }.take(10)

        val pageRequest = PageRequest(
            pageNumber = 1,
            pageSize = 10,
            pageSort = listOf(
                PageSort(sortField = "integer_required", sortDirection = PageSortDirection.DESC)
            )
        )

        val pagedResult = repository.paged(pageable = pageRequest)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 10
        pagedResult.contentList[0].integerRequired shouldBe filtered[0].integerRequired
        pagedResult.contentList[1].integerRequired shouldBe filtered[1].integerRequired
        pagedResult.contentList[2].integerRequired shouldBe filtered[2].integerRequired
        pagedResult.contentList[3].integerRequired shouldBe filtered[3].integerRequired
        pagedResult.contentList[4].integerRequired shouldBe filtered[4].integerRequired
        pagedResult.contentList[5].integerRequired shouldBe filtered[5].integerRequired
        pagedResult.contentList[6].integerRequired shouldBe filtered[6].integerRequired
        pagedResult.contentList[7].integerRequired shouldBe filtered[7].integerRequired
        pagedResult.contentList[8].integerRequired shouldBe filtered[8].integerRequired
        pagedResult.contentList[9].integerRequired shouldBe filtered[9].integerRequired
    }

    @Test
    fun `should apply sort only to existent columns`() = runTest {
        val filtered = entities.sortedByDescending { it.integerRequired }.take(10)

        val pageRequest = PageRequest(
            pageNumber = 1,
            pageSize = 10,
            pageSort = listOf(
                PageSort(sortField = "integer_required", sortDirection = PageSortDirection.DESC),
                PageSort(sortField = "qwerty", sortDirection = PageSortDirection.DESC),
            )
        )

        val pagedResult = repository.paged(pageable = pageRequest)

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 10
        pagedResult.contentList[0].integerRequired shouldBe filtered[0].integerRequired
        pagedResult.contentList[1].integerRequired shouldBe filtered[1].integerRequired
        pagedResult.contentList[2].integerRequired shouldBe filtered[2].integerRequired
        pagedResult.contentList[3].integerRequired shouldBe filtered[3].integerRequired
        pagedResult.contentList[4].integerRequired shouldBe filtered[4].integerRequired
        pagedResult.contentList[5].integerRequired shouldBe filtered[5].integerRequired
        pagedResult.contentList[6].integerRequired shouldBe filtered[6].integerRequired
        pagedResult.contentList[7].integerRequired shouldBe filtered[7].integerRequired
        pagedResult.contentList[8].integerRequired shouldBe filtered[8].integerRequired
        pagedResult.contentList[9].integerRequired shouldBe filtered[9].integerRequired
    }

    @Test
    fun `should combine multiple filters in paged results`() = runTest {
        repository.insert(
            EntityTestEntity(
                stringRequired = "João Silva",
                integerRequired = 100,
                enumerationRequired = Gender.CIS_MALE
            )
        )
        repository.insert(
            EntityTestEntity(
                stringRequired = "Maria Silva",
                integerRequired = 100,
                enumerationRequired = Gender.CIS_FEMALE
            )
        )
        repository.insert(
            EntityTestEntity(
                stringRequired = "João Santos",
                integerRequired = 200,
                enumerationRequired = Gender.CIS_MALE
            )
        )

        val pagedResult = repository.paged(
            stringValue = "silva",
            intValue = 100,
            enumValue = Gender.CIS_MALE
        )

        pagedResult.shouldNotBeNull()
        pagedResult.contentList shouldHaveSize 1
        pagedResult.totalItems shouldBe 1
        pagedResult.contentList[0].stringRequired shouldBe "João Silva"
        pagedResult.contentList[0].integerRequired shouldBe 100
        pagedResult.contentList[0].enumerationRequired shouldBe Gender.CIS_MALE
    }

    @Test
    fun `should return empty page when no results match filters`() = runTest {
        val pagedResult = repository.paged(stringValue = "pedro")

        pagedResult.shouldNotBeNull()
        pagedResult.contentList.shouldBeEmpty()
        pagedResult.totalItems shouldBe 0
        pagedResult.totalPages shouldBe 0
    }

}