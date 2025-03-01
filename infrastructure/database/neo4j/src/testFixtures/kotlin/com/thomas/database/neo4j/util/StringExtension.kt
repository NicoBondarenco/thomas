package com.thomas.database.neo4j.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME

fun String.toZonedDateTimeStart(): ZonedDateTime = ZonedDateTime.parse("${this}T00:00:00.000000Z", ISO_ZONED_DATE_TIME)

fun String.toZonedDateTimeEnd(): ZonedDateTime = ZonedDateTime.parse("${this}T23:59:59.999999Z", ISO_ZONED_DATE_TIME)

fun String.toZonedDateTime(): ZonedDateTime = ZonedDateTime.parse(this, ISO_ZONED_DATE_TIME)