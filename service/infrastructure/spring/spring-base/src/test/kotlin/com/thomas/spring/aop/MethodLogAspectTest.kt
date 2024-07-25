package com.thomas.spring.aop

import com.thomas.core.aop.MaskField
import com.thomas.core.aop.MethodLog
import com.thomas.core.aop.MethodLogLevel
import com.thomas.core.aop.MethodLogLevel.INFO
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.security.SecurityUser
import com.thomas.core.random.randomString
import com.thomas.spring.configuration.JacksonConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID
import java.util.UUID.fromString
import java.util.UUID.randomUUID
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.cachedLoggerOf
import org.apache.logging.log4j.kotlin.loggerOf
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode.STRICT

@TestInstance(PER_CLASS)
class MethodLogAspectTest {

    companion object {
        private const val RESULT_PREFIX = "[result] = "

        @JvmStatic
        fun simpleTypes(): List<Arguments> = listOf(
            Arguments.of("intAttribute", Int::class.java, nextInt(100, 99999), true),
            Arguments.of("intNull", Int::class.java, null, true),
            Arguments.of("intAttribute", Int::class.java, nextInt(100, 99999), false),
            Arguments.of("intNull", Int::class.java, null, false),
            Arguments.of("charAttribute", Char::class.java, nextInt(48, 122).toChar(), true),
            Arguments.of("charNull", Char::class.java, null, true),
            Arguments.of("charAttribute", Char::class.java, nextInt(48, 122).toChar(), false),
            Arguments.of("charNull", Char::class.java, null, false),
            Arguments.of("shortAttribute", Short::class.java, nextInt(1, 999).toShort(), true),
            Arguments.of("shortNull", Short::class.java, null, true),
            Arguments.of("shortAttribute", Short::class.java, nextInt(1, 999).toShort(), false),
            Arguments.of("shortNull", Short::class.java, null, false),
            Arguments.of("longAttribute", Long::class.java, nextLong(100000, 999999), true),
            Arguments.of("longNull", Long::class.java, null, true),
            Arguments.of("longAttribute", Long::class.java, nextLong(100000, 999999), false),
            Arguments.of("longNull", Long::class.java, null, false),
            Arguments.of("floatAttribute", Float::class.java, nextFloat(), true),
            Arguments.of("floatNull", Float::class.java, null, true),
            Arguments.of("floatAttribute", Float::class.java, nextFloat(), false),
            Arguments.of("floatNull", Float::class.java, null, false),
            Arguments.of("doubleAttribute", Double::class.java, nextDouble(), true),
            Arguments.of("doubleNull", Double::class.java, null, true),
            Arguments.of("doubleAttribute", Double::class.java, nextDouble(), false),
            Arguments.of("doubleNull", Double::class.java, null, false),
            Arguments.of("booleanAttribute", Boolean::class.java, nextBoolean(), true),
            Arguments.of("booleanNull", Boolean::class.java, null, true),
            Arguments.of("booleanAttribute", Boolean::class.java, nextBoolean(), false),
            Arguments.of("booleanNull", Boolean::class.java, null, false),
            Arguments.of("stringAttribute", String::class.java, randomString(nextInt(5, 20)), true),
            Arguments.of("stringNull", String::class.java, null, true),
            Arguments.of("stringAttribute", String::class.java, randomString(nextInt(5, 20)), false),
            Arguments.of("stringNull", String::class.java, null, false),
            Arguments.of("uuidAttribute", UUID::class.java, randomUUID(), true),
            Arguments.of("uuidNull", UUID::class.java, null, true),
            Arguments.of("uuidAttribute", UUID::class.java, randomUUID(), false),
            Arguments.of("uuidNull", UUID::class.java, null, false),
        )

    }

    private val realLogger = loggerOf(MethodLogAspectTest::class.java)

    private val user = SecurityUser(
        randomUUID(),
        "Security",
        "User",
        "security.user@test.com",
        "16988776655",
        null,
        LocalDate.now(ZoneOffset.UTC),
        CIS_MALE,
        true,
        listOf(),
        listOf(),
    )
    private val mapper = JacksonConfiguration().aspectMapper()
    private val logger = mockk<KotlinLogger>()
    private val aspect = MethodLogAspect(mapper)

    private var logContent = ""
    private var logLevel = Level.ALL

    @BeforeAll
    fun beforeAll() {
        currentUser = user
        every { logger.log(any<Level>(), any<String>()) } answers {
            val level = it.invocation.args[0] as Level
            val log = it.invocation.args[1] as String
            logContent = log
            logLevel = level
            realLogger.info(log)
        }
        mockkStatic("org.apache.logging.log4j.kotlin.LoggingFactoryKt")
        every { cachedLoggerOf(any()) } returns logger
    }

    @BeforeEach
    fun beforeEach() {
        logContent = ""
        logLevel = Level.ALL
    }

    @Test
    fun `Log full method`() {
        val result = Person()
        val arguments = arrayOf(
            CallArgument(
                argument = randomUUID().toString(),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns true
                },
                name = "personId",
                type = UUID::class.java
            ),
            CallArgument(
                argument = nextInt(1000000, 9999999).toString(),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns true
                },
                name = "personDocument",
                type = String::class.java
            ),
            CallArgument(
                argument = nextInt(100, 999),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns false
                },
                name = "amount",
                type = Int::class.java
            ),
            CallArgument(
                argument = Random.nextDouble(100.0, 999.9),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns false
                },
                name = "amount",
                type = Double::class.java
            ),
            CallArgument(
                argument = Person(),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns true
                },
                name = "requestData",
                type = Person::class.java
            ),
        )
        val point = configureJoinPoint(
            callArguments = arguments,
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertEquals(logLevel, Level.INFO)
        assertTrue(logContent.startsWith("[UserId=${user.userId}] - "))
        assertTrue(logContent.contains("${MethodLogAspectTest::class.java.name}.simpleMethod: Person"))
        assertTrue(logContent.contains("parameter[0] -> personId: UUID = ************************************"))
        assertTrue(logContent.contains("parameter[1] -> personDocument: String = *******"))
        assertTrue(logContent.contains("parameter[2] -> amount: int = ${arguments[2].argument}"))
        assertTrue(logContent.contains("parameter[3] -> amount: double = ${arguments[3].argument}"))
        JSONAssert.assertEquals(mapper.writeValueAsString(arguments[4].argument), logContent.parameterContent(4), STRICT)
        JSONAssert.assertEquals(mapper.writeValueAsString(result), logContent.resultContent(), STRICT)
    }

    @ParameterizedTest
    @MethodSource("simpleTypes")
    fun `Log method with simple type parameter`(name: String, type: Class<*>, value: Any?, mask: Boolean) {
        val result = Person()
        val arguments = arrayOf(
            CallArgument(
                argument = value,
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns mask
                },
                name = name,
                type = type
            )
        )
        val point = configureJoinPoint(
            logUser = false,
            logResult = false,
            methodName = "simpleType",
            callArguments = arguments,
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        val argValue = value?.let {
            if (mask) {
                it.toString().let { v -> "*".repeat(v.length) }
            } else {
                it.toString()
            }
        } ?: "null"

        println("parameter[0] -> $name: ${type.simpleName} = $argValue")
        println(logContent)
        assertTrue(logContent.contains("parameter[0] -> $name: ${type.simpleName} = $argValue"))
    }

    @Test
    fun `Log method with null return`() {
        val arguments = arrayOf(
            CallArgument(
                argument = randomUUID().toString(),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns false
                },
                name = "personId",
                type = UUID::class.java
            ),
        )
        val point = configureJoinPoint(
            callArguments = arguments,
            returnArgument = null,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertTrue(logContent.startsWith("[UserId=${user.userId}] - "))
        assertTrue(logContent.contains("${MethodLogAspectTest::class.java.name}.simpleMethod: Person"))
        assertTrue(logContent.contains("[result] = null"))
    }

    @Test
    fun `Log method without parameters`() {
        val result = Person()
        val point = configureJoinPoint(
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertFalse(logContent.contains("parameter[0]"))
    }

    @Test
    fun `Log method without log parameters`() {
        val result = Person()
        val arguments = arrayOf(
            CallArgument(
                argument = randomUUID().toString(),
                parameter = mockk<Parameter>().apply {
                    every { this@apply.isAnnotationPresent(MaskField::class.java) } returns false
                },
                name = "personId",
                type = UUID::class.java
            ),
        )
        val point = configureJoinPoint(
            logParameters = false,
            callArguments = arguments,
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertFalse(logContent.contains("parameter[0]"))
    }

    @Test
    fun `Log method without user`() {
        val result = Person()
        val point = configureJoinPoint(
            logUser = false,
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertFalse(logContent.contains("[UserId=${user.userId}] - "))
    }

    @ParameterizedTest
    @EnumSource(MethodLogLevel::class)
    fun `Log method by level`(level: MethodLogLevel) {
        val result = Person()
        val point = configureJoinPoint(
            logLevel = level,
            returnArgument = result,
            returnType = Person::class.java,
        )

        aspect.methodLogging(point)

        assertEquals(logLevel, level.level)
    }

    @Suppress("LongParameterList")
    private fun configureJoinPoint(
        logLevel: MethodLogLevel = INFO,
        logParameters: Boolean = true,
        logResult: Boolean = true,
        maskResult: Boolean = true,
        logUser: Boolean = true,
        methodName: String = "simpleMethod",
        callArguments: Array<CallArgument> = arrayOf(),
        returnArgument: Any?,
        returnType: Class<*>,
    ): ProceedingJoinPoint = mockk<ProceedingJoinPoint>().apply {
        val method = mockk<Method>().also {
            every { it.getAnnotation(MethodLog::class.java) } returns MethodLog(
                logLevel = logLevel,
                logParameters = logParameters,
                logResult = logResult,
                maskResult = maskResult,
                logUser = logUser,
            )
            every { it.parameters } returns callArguments.map { it.parameter }.toTypedArray()
        }
        val methodSignature = mockk<MethodSignature>().also {
            every { it.method } returns method
            every { it.name } returns methodName
            every { it.parameterNames } returns callArguments.map { it.name }.toTypedArray()
            every { it.parameterTypes } returns callArguments.map { it.type }.toTypedArray()
            every { it.returnType } returns returnType
        }
        every { this@apply.target } returns MethodLogAspectTest()
        every { this@apply.signature } returns methodSignature
        every { this@apply.args } returns callArguments.map { it.argument }.toTypedArray()
        every { this@apply.proceed() } returns returnArgument
    }

    private data class CallArgument(
        val argument: Any?,
        val parameter: Parameter,
        val name: String,
        val type: Class<*>,
    )

    private data class Person(
        val personId: UUID = fromString("4fe78258-d744-4fb0-97a6-3cdf4e850e3a"),
        val fullName: String = "Anthony Stark",
        @MaskField val personEmail: String = "iron.man@email.com",
        val personGender: Gender = CIS_MALE,
        val birthDate: LocalDate = LocalDate.of(1990, 4, 28),
        @MaskField val secretNumber: Int = nextInt(10, 99),
        @MaskField val secretCode: UUID? = fromString("99c081c5-9bbd-4491-be1f-f0dab18f4147"),
    )

    private fun String.parameterContent(index: Int) = this
        .substring(this.indexOf("parameter[$index]")).let { str ->
            str.substring(str.indexOf("=") + 2, str.indexOf(System.lineSeparator()))
        }

    private fun String.resultContent() = this.substring(this.indexOf(RESULT_PREFIX) + RESULT_PREFIX.length)

}
