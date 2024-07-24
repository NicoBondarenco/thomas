package com.thomas.core.aop

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class MethodLog(
    val logParameters: Boolean = true,
    val logResult: Boolean = false,
    val maskResult: Boolean = false,
    val logUser: Boolean = false,
)
