package com.thomas.exposed.table

import com.thomas.core.extension.isBetween
import com.thomas.exposed.exception.ExposedException
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType

class OffsetDateTimeColumnType(
    private val precision: Int
) : ColumnType(), IDateColumnType {

    companion object {
        private const val MIN_PRECISION = 0
        private const val MAX_PRECISION = 9
    }

    init {
        if (!precision.isBetween(MIN_PRECISION, MAX_PRECISION)) throw ExposedException("precision must be between 0 and 9")
    }

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss${precision.precisionFormat()}X"
    )

    private fun Int.precisionFormat() = "S".repeat(this)
        .takeIf { it.isNotEmpty() }
        ?.let { ".$it" }
        ?: ""

    override val hasTimePart: Boolean = true

    override fun sqlType(): String = "TIMESTAMP($precision) WITH TIME ZONE"

    override fun nonNullValueToString(
        value: Any
    ): String = "'${formatter.format(value.toOffsetDateTime())}'"

    override fun valueFromDB(
        value: Any
    ): OffsetDateTime = value.toOffsetDateTime()

    override fun notNullValueToDB(
        value: Any
    ): OffsetDateTime = value.toOffsetDateTime()

    override fun readObject(
        rs: ResultSet,
        index: Int
    ): OffsetDateTime? = rs.getObject(index, OffsetDateTime::class.java)

    private fun Any.toOffsetDateTime(): OffsetDateTime = if (this is OffsetDateTime) {
        this
    } else {
        throw ExposedException("value $this (${this::class}) is not of type OffsetDateTime")
    }

}
