package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getLocalTimeOrNull
import java.time.LocalTime
import kotlin.reflect.KParameter
import org.bson.Document

class LocalTimeKParameterParser : KParameterParser<LocalTime>(LocalTime::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): LocalTime? = document.getLocalTimeOrNull(parameter.name!!)

}