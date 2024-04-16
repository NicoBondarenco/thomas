package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getLongOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class LongKParameterParser : KParameterParser<Long>(Long::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): Long? = document.getLongOrNull(parameter.name!!)

}