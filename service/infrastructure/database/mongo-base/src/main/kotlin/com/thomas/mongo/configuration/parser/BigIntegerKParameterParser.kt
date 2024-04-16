package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getBigIntegerOrNull
import java.math.BigInteger
import kotlin.reflect.KParameter
import org.bson.Document

class BigIntegerKParameterParser : KParameterParser<BigInteger>(BigInteger::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): BigInteger? = document.getBigIntegerOrNull(parameter.name!!)

}