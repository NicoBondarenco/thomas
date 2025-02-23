package com.thomas.database.neo4j.converter

import java.math.BigInteger
import org.neo4j.ogm.typeconversion.AttributeConverter

class BigIntegerConverter : AttributeConverter<BigInteger, Long> {

    override fun toGraphProperty(
        value: BigInteger?
    ): Long? = value?.toLong()

    override fun toEntityAttribute(
        value: Long?
    ): BigInteger? = value?.toBigInteger()

}
