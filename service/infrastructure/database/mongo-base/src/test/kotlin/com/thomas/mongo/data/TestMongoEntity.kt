package com.thomas.mongo.data

import com.thomas.core.model.entity.BaseEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

val foundDateMin = Instant.parse("1990-04-28T00:00:00.000Z")
val foundDateMax = Instant.parse("1990-04-30T23:59:59.999Z")

private fun generateValidOffsetDateTime() = nextLong(
    foundDateMin.toEpochMilli(),
    foundDateMax.toEpochMilli(),
).let { Instant.ofEpochMilli(it).atOffset(UTC) }

val entityListFoundData = (1..20).map {
    TestMongoEntity(
        stringValue = randomUUID().toString(),
        datetimeOffset = generateValidOffsetDateTime()
    )
}

val entityListNotFoundData = (1..15).map { value ->
    TestMongoEntity(
        stringValue = randomUUID().toString(),
        datetimeOffset = generateValidOffsetDateTime().let {
            if (value % 2 == 0) {
                it.plusDays(3)
            } else {
                it.minusDays(3)
            }
        }
    )
}

enum class TestMongoEnum {
    VALUE_ONE,
    VALUE_TWO,
}

data class TestMongoEntity(
    override val id: UUID = randomUUID(),
    val stringValue: String = randomUUID().toString(),
    val booleanValue: Boolean = nextBoolean(),
    val intValue: Int = nextInt(1, 50),
    val longValue: Long = nextLong(1, 50),
    val doubleValue: Double = nextDouble(1.0, 50.0),
    val bigDecimal: BigDecimal = BigDecimal.valueOf(nextDouble(1.0, 50.0)),
    val bigInteger: BigInteger = BigInteger.valueOf(nextLong(1, 50)),
    val dateValue: LocalDate = LocalDate.now(),
    val timeValue: LocalTime = LocalTime.now().withNano(0),
    val datetimeValue: LocalDateTime = LocalDateTime.now().withNano(0),
    val datetimeOffset: OffsetDateTime = OffsetDateTime.now(UTC).withNano(0),
    val enumValue: TestMongoEnum = TestMongoEnum.entries.random(),
    val childEntity: ChildEntity = ChildEntity(),
    val stringList: List<String> = listOf(UUID.randomUUID().toString()),
    val childList: List<ChildEntity> = listOf(ChildEntity(), ChildEntity()),
    val uuidEmpty: UUID? = null,
    val stringEmpty: String? = null,
    val booleanEmpty: Boolean? = null,
    val intEmpty: Int? = null,
    val longEmpty: Long? = null,
    val doubleEmpty: Double? = null,
    val bigdecimalEmpty: BigDecimal? = null,
    val bigintegerEmpty: BigInteger? = null,
    val dateEmpty: LocalDate? = null,
    val timeEmpty: LocalTime? = null,
    val datetimeEmpty: LocalDateTime? = null,
    val offsetEmpty: OffsetDateTime? = null,
    val enumNull: TestMongoEnum? = null,
    val childValue: ChildEntity? = null,
    val stringsNull: List<String?>? = null,
    val childrenNull: List<ChildEntity?>? = null,
) : BaseEntity<TestMongoEntity>()

data class ChildEntity(
    override val id: UUID = randomUUID(),
    val childName: String = randomUUID().toString(),
    val anotherEntity: AnotherEntity = AnotherEntity(),
    val anotherEmpty: AnotherEntity? = null,
    val anotherList: List<AnotherEntity> = listOf(AnotherEntity(), AnotherEntity()),
    val listEmpty: List<AnotherEntity>? = null,
    val listNull: List<AnotherEntity?> = listOf(AnotherEntity(), AnotherEntity(), null),
) : BaseEntity<ChildEntity>()

data class AnotherEntity(
    override val id: UUID = randomUUID(),
    val name: String = randomUUID().toString(),
) : BaseEntity<AnotherEntity>()
