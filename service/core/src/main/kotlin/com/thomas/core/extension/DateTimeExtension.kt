package com.thomas.core.extension

import com.thomas.core.exception.InstantEpochNanoException
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

val ISO_OFFSET_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_DATE

fun LocalDate.toIsoDate(): String = ISO_DATE_FORMATTER.format(this)

fun OffsetDateTime.toIsoOffsetDateTime(): String = ISO_OFFSET_DATE_TIME_FORMATTER.format(this)

private val minEpochNanoInstant = Instant.parse("1900-01-01T00:00:00Z")
private val maxEpochNanoInstant = Instant.parse("2099-12-31T23:59:59.999999999Z")

fun Instant.epochNanoSeconds() = if (this.isBetween(minEpochNanoInstant, maxEpochNanoInstant)) {
    "${this.epochSecond}${this.nano.toString().padStart(9, '0')}".toLong()
} else {
    throw InstantEpochNanoException("Instant is not between bounds ($minEpochNanoInstant - $maxEpochNanoInstant)")
}

private const val minEpochNanoValue = -2208988800000000000
private const val maxEpochNanoValue = 4102444799999999999
private const val epochNanoDivisor = 1_000_000_000

fun instantOfEpochNanoSeconds(
    epochNanos: Long
): Instant = if (epochNanos.isBetween(minEpochNanoValue, maxEpochNanoValue)) {
    val epochSecond = (epochNanos / epochNanoDivisor)
    val nanos = (epochNanos % epochNanoDivisor).toPositive()
    Instant.ofEpochSecond(epochSecond, nanos)
} else {
    throw InstantEpochNanoException("EpochNanoSeconds is not between bounds ($minEpochNanoInstant - $maxEpochNanoInstant)")
}

fun Instant.isBetween(min: Instant, max: Instant): Boolean =
    (this.isAfter(min) && this.isBefore(max)) || (this == min) || (this == max)