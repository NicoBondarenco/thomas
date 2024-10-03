package com.thomas.mongodb.extension

import kotlin.reflect.KProperty

const val OBJECT_ID_PARAMETER_NAME = "_id"

val <T> KProperty<T>.mongoParameter
    get() = this.name.mongoParameter

val String.mongoParameter
    get() = "\$$this"