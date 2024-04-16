package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getStringOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class StringKParameterParser : KParameterParser<String>(String::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): String? = document.getStringOrNull(parameter.name!!)

}