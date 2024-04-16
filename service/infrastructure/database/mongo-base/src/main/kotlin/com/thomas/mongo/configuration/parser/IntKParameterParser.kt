package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getIntegerOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class IntKParameterParser : KParameterParser<Int>(Int::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): Int? = document.getIntegerOrNull(parameter.name!!)

}