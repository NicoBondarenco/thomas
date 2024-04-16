package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getDoubleOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class DoubleKParameterParser : KParameterParser<Double>(Double::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): Double? = document.getDoubleOrNull(parameter.name!!)

}