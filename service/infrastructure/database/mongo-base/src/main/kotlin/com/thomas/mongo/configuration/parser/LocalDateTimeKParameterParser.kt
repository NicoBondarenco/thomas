package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getLocalDateTimeOrNull
import java.time.LocalDateTime
import kotlin.reflect.KParameter
import org.bson.Document

class LocalDateTimeKParameterParser : KParameterParser<LocalDateTime>(LocalDateTime::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): LocalDateTime? = document.getLocalDateTimeOrNull(parameter.name!!)

}