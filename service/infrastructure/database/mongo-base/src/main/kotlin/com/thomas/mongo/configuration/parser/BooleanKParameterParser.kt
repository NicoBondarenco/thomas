package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getBooleanOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class BooleanKParameterParser : KParameterParser<Boolean>(Boolean::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): Boolean? = document.getBooleanOrNull(parameter.name!!)

}