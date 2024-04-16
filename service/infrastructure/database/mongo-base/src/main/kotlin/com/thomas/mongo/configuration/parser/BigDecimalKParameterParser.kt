package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getBigDecimalOrNull
import java.math.BigDecimal
import kotlin.reflect.KParameter
import org.bson.Document

class BigDecimalKParameterParser : KParameterParser<BigDecimal>(BigDecimal::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): BigDecimal? = document.getBigDecimalOrNull(parameter.name!!)

}