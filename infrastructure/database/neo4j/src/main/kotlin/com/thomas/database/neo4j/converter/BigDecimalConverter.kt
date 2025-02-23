package com.thomas.database.neo4j.converter

import java.math.BigDecimal
import org.neo4j.ogm.typeconversion.AttributeConverter

class BigDecimalConverter : AttributeConverter<BigDecimal, Double> {

    override fun toGraphProperty(
        value: BigDecimal?
    ): Double? = value?.toDouble()

    override fun toEntityAttribute(
        value: Double?
    ): BigDecimal? = value?.toBigDecimal()

}
