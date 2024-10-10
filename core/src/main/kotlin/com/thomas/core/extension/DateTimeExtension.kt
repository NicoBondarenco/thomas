package com.thomas.core.extension

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

val ISO_OFFSET_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_DATE

fun LocalDate.toIsoDate(): String = ISO_DATE_FORMATTER.format(this)

fun OffsetDateTime.toIsoOffsetDateTime(): String = ISO_OFFSET_DATE_TIME_FORMATTER.format(this)
