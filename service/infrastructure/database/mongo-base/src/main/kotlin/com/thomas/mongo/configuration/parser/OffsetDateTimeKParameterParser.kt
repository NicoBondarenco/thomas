package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getOffsetDateTimeOrNull
import java.time.OffsetDateTime
import kotlin.reflect.KParameter
import org.bson.Document

class OffsetDateTimeKParameterParser : KParameterParser<OffsetDateTime>(OffsetDateTime::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): OffsetDateTime? = document.getOffsetDateTimeOrNull(parameter.name!!)

}