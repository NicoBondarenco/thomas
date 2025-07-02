package com.thomas.database.komapper.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.general.Gender
import com.thomas.core.util.BooleanUtils.randomBoolean
import com.thomas.core.util.DateUtils.randomLocalDate
import com.thomas.core.util.DateUtils.randomLocalDateTime
import com.thomas.core.util.DateUtils.randomLocalTime
import com.thomas.core.util.DateUtils.randomOffsetDateTime
import com.thomas.core.util.DateUtils.randomOffsetTime
import com.thomas.core.util.NumberUtils.randomBigDecimal
import com.thomas.core.util.NumberUtils.randomBigInteger
import com.thomas.core.util.NumberUtils.randomDouble
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import com.thomas.core.util.StringUtils.randomString
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.OffsetTime
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class EntityTestEntity(
    override val id: UUID = randomUUID(),
    val stringRequired: String = randomString(),
    val stringNullable: String? = listOf(randomString(), null).random(),
    val integerRequired: Int = randomInteger(),
    val integerNullable: Int? = listOf(randomInteger(), null).random(),
    val longRequired: Long = randomLong(),
    val longNullable: Long? = listOf(randomLong(), null).random(),
    val doubleRequired: Double = randomDouble(),
    val doubleNullable: Double? = listOf(randomDouble(), null).random(),
    val bigintegerRequired: BigInteger = randomBigInteger(),
    val bigintegerNullable: BigInteger? = listOf(randomBigInteger(), null).random(),
    val bigdecimalRequired: BigDecimal = randomBigDecimal(),
    val bigdecimalNullable: BigDecimal? = listOf(randomBigDecimal(), null).random(),
    val booleanRequired: Boolean = randomBoolean(),
    val booleanNullable: Boolean? = listOf(randomBoolean(), null).random(),
    val enumerationRequired: Gender = Gender.entries.random(),
    val enumerationNullable: Gender? = listOf(Gender.entries.random(), null).random(),
    val localdateRequired: LocalDate = randomLocalDate(),
    val localdateNullable: LocalDate? = listOf(randomLocalDate(), null).random(),
    val localdatetimeRequired: LocalDateTime = randomLocalDateTime(),
    val localdatetimeNullable: LocalDateTime? = listOf(randomLocalDateTime(), null).random(),
    val localtimeRequired: LocalTime = randomLocalTime(),
    val localtimeNullable: LocalTime? = listOf(randomLocalTime(), null).random(),
    val offsetdatetimeRequired: OffsetDateTime = randomOffsetDateTime(),
    val offsetdatetimeNullable: OffsetDateTime? = listOf(randomOffsetDateTime(), null).random(),
    val uuidRequired: UUID = randomUUID(),
    val uuidNullable: UUID? = listOf(randomUUID(), null).random(),
    val createdAt: OffsetDateTime = now(UTC),
    val updatedAt: OffsetDateTime = now(UTC),
) : BaseEntity<EntityTestEntity>()