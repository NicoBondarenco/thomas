package com.thomas.management.data.neo4j.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun String.toOffsetDateTime(): OffsetDateTime = OffsetDateTime.parse(this, ISO_OFFSET_DATE_TIME)