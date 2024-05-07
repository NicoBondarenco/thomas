package com.thomas.exposed.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

fun <ID : Comparable<ID>> Entity<ID>.valuesJson(
    mapper: ObjectMapper
) = this.readValues.let { row ->
    row.fieldIndex.mapValues {
        row.getOrNull(it.key)?.let { value ->
            if (value is EntityID<*>) {
                value.value
            } else {
                value
            }
        }
    }.mapKeys {
        (it.key as Column).name
    }.let { mapper.writeValueAsString(it) }
}
