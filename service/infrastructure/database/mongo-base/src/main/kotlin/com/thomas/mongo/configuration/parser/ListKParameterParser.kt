package com.thomas.mongo.configuration.parser

import com.thomas.core.model.entity.BaseEntity
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

abstract class ListKParameterParser : KParameterParser<List<*>>(List::class) {

    protected fun KParameter.isList() =
        this.type.jvmErasure.isSubclassOf(List::class)

    protected fun KParameter.isBaseEntityList() =
        this.type.arguments.first().type?.jvmErasure?.isSubclassOf(BaseEntity::class) == true

}