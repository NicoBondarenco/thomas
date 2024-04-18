package com.thomas.mongo.configuration.parser

import com.thomas.core.model.entity.BaseEntity
import com.thomas.mongo.extension.getDocumentOrNull
import com.thomas.mongo.extension.toEntity
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import org.bson.Document

class BaseEntityKParameterParser : KParameterParser<BaseEntity<*>>(BaseEntity::class) {

    override fun accept(parameter: KParameter): Boolean =
        parameter.kclass().isSubclassOf(BaseEntity::class)

    override fun parse(
        parameter: KParameter,
        document: Document
    ): BaseEntity<*>? = document.getDocumentOrNull(parameter.name!!)
        ?.toEntity(parameter.kclass() as KClass<BaseEntity<*>>)

}