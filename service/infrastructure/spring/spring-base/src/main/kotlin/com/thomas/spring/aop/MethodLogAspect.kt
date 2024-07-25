package com.thomas.spring.aop

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.core.aop.MaskField
import com.thomas.core.aop.MethodLog
import com.thomas.core.context.SessionContextHolder.currentUserId
import java.lang.System.lineSeparator
import java.lang.reflect.Parameter
import java.util.UUID
import kotlin.reflect.KClass
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.cachedLoggerOf
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Aspect
@Component
class MethodLogAspect(
    @Qualifier("aspect-mapper") private val mapper: ObjectMapper,
) {

    companion object {
        private val SIMPLE_TYPES = setOf(
            Int::class,
            Char::class,
            Short::class,
            Long::class,
            Float::class,
            Double::class,
            Boolean::class,
            String::class,
            UUID::class,
        )
    }

    @Around(value = "@annotation(com.thomas.core.aop.MethodLog)")
    fun methodLogging(point: ProceedingJoinPoint): Any? {
        val logger = point.logger()
        val annotation = point.logAnnotation()
        val result = point.proceed()
        logger.log(annotation.logLevel.level, "${annotation.userIdLog()}${point.methodLog()}${point.parametersLog(annotation)}${annotation.resultLog(result)}")
        return result
    }

    private fun JoinPoint.logger(): KotlinLogger = cachedLoggerOf(this.target::class.java)

    private fun JoinPoint.logAnnotation() = (this.signature as MethodSignature).method.getAnnotation(MethodLog::class.java)

    private fun MethodLog.userIdLog() = if (this.logUser) {
        "[UserId=$currentUserId] - "
    } else {
        ""
    }

    private fun JoinPoint.methodLog() = "${this.className()}.${this.methodName()}: ${this.methodSignature().returnType.simpleName}"

    private fun JoinPoint.className() = this.target::class.qualifiedName

    private fun JoinPoint.methodName() = this.signature.name

    private fun JoinPoint.methodSignature() = (this.signature as MethodSignature)

    private fun JoinPoint.parametersLog(methodLog: MethodLog): String = if (methodLog.logParameters) {
        val method = this.methodSignature()
        this.args.mapIndexed { index, arg ->
            val argumentLog = arg.argumentLog(method.method.parameters[index].maskValue())
            "${lineSeparator()}\tparameter[$index] -> ${method.parameterNames[index]}: ${method.parameterTypes[index].simpleName} = $argumentLog"
        }.joinToString("")
    } else {
        ""
    }

    private fun Any?.argumentLog(mask: Boolean) = this?.let { arg ->
        arg.takeIf {
            it::class.isSimpleType()
        }?.simpleArgumentLog(mask) ?: mapper.writeValueAsString(arg)
    } ?: "null"

    private fun Any.simpleArgumentLog(mask: Boolean): String = if (mask) {
        this.toString().let { "*".repeat(it.length) }
    } else {
        this.toString()
    }

    private fun <T : Any> KClass<T>.isSimpleType() = this in SIMPLE_TYPES

    private fun Parameter.maskValue() = this.isAnnotationPresent(MaskField::class.java)

    private fun MethodLog.resultLog(result: Any?): String = if (this.logResult) {
        "${lineSeparator()}\t[result] = ${result.argumentLog(this.maskResult)}"
    } else {
        ""
    }

}
