package com.thomas.mongo.configuration.parser

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure
import org.bson.Document

abstract class KParameterParser<T : Any>(
    private val klass: KClass<T>
) {

    open fun accept(parameter: KParameter): Boolean =
        parameter.kclass() == klass

    abstract fun parse(
        parameter: KParameter,
        document: Document
    ): T?

    protected fun KParameter.kclass() = this.type.jvmErasure

}