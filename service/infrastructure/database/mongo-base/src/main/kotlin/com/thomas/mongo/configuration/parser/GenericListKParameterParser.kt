package com.thomas.mongo.configuration.parser

import kotlin.reflect.KParameter
import org.bson.Document

class GenericListKParameterParser : ListKParameterParser() {

    override fun accept(parameter: KParameter): Boolean =
        parameter.isList() && !parameter.isBaseEntityList()

    override fun parse(
        parameter: KParameter,
        document: Document
    ): List<*>? = (document[parameter.name!!] as List<*>?)

}