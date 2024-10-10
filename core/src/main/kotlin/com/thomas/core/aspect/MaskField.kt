package com.thomas.core.aspect

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.annotation.AnnotationTarget.FIELD

@Target(VALUE_PARAMETER, FIELD)
@Retention(RUNTIME)
annotation class MaskField
