package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getLocalDateOrNull
import java.time.LocalDate
import kotlin.reflect.KParameter
import org.bson.Document

class LocalDateKParameterParser : KParameterParser<LocalDate>(LocalDate::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): LocalDate? = document.getLocalDateOrNull(parameter.name!!)

}