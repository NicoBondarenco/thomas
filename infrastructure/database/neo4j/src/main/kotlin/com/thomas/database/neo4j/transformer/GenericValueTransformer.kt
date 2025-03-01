package com.thomas.database.neo4j.transformer

import java.math.BigDecimal
import java.math.BigInteger
import java.time.OffsetDateTime
import java.util.UUID
import org.neo4j.ogm.cypher.PropertyValueTransformer

class GenericValueTransformer(
    private val defaultTransformer: PropertyValueTransformer
) : PropertyValueTransformer {

    companion object {
        private val CONVERTABLE_VALUES = mapOf<Class<*>, (Any) -> Any>(
            BigInteger::class.java to { (it as BigInteger).toLong() },
            BigDecimal::class.java to { (it as BigDecimal).toDouble() },
            UUID::class.java to { (it as UUID).toString() },
            OffsetDateTime::class.java to { (it as OffsetDateTime).toZonedDateTime() },
        )
    }

    override fun transformPropertyValue(
        propertyValue: Any?
    ): Any? = propertyValue?.let { value ->
        CONVERTABLE_VALUES[value::class.java]?.invoke(value) ?: defaultTransformer.transformPropertyValue(value)
    }

}