package com.thomas.mongo.configuration.parser

import com.thomas.mongo.extension.getUUIDOrNull
import java.util.UUID
import kotlin.reflect.KParameter
import org.bson.Document

class UUIDKParameterParser : KParameterParser<UUID>(UUID::class) {

    override fun parse(
        parameter: KParameter,
        document: Document
    ): UUID? = document.getUUIDOrNull(parameter.name!!)

}