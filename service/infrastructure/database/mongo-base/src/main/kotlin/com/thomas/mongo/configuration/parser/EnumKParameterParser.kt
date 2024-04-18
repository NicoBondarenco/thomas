package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getEnumOrNull
import kotlin.reflect.KParameter
import org.bson.Document

class EnumKParameterParser : KParameterParser<Enum<*>>(Enum::class) {

    override fun accept(parameter: KParameter): Boolean =
        parameter.kclass().java.isEnum

    override fun parse(
        parameter: KParameter,
        document: Document
    ): Enum<*>? = document.getEnumOrNull(parameter.name!!, parameter.kclass().java as Class<Enum<*>>)

}