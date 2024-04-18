package com.thomas.mongo.configuration.parser

import com.thomas.core.model.entity.BaseEntity
import com.thomas.mongo.extension.toEntity
import kotlin.reflect.KParameter
import org.bson.Document

class BaseEntityListKParameterParser : ListKParameterParser() {

    override fun accept(parameter: KParameter): Boolean =
        parameter.isList() && parameter.isBaseEntityList()

    override fun parse(
        parameter: KParameter,
        document: Document
    ): List<BaseEntity<*>>? = (document[parameter.name!!] as List<Document>?)?.mapNotNull { it.toEntity() }

}