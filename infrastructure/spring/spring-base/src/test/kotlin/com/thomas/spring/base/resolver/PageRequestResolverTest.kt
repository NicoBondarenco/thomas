package com.thomas.spring.base.resolver


import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.base.exception.RequestException
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidField
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidNumber
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSize
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSort
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import kotlin.reflect.KClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest

class PageRequestResolverTest {

    companion object {

        @Suppress("UnusedPrivateMember")
        @JvmStatic
        private fun classList() = listOf(
            Arguments.of(String::class.java),
            Arguments.of(OffsetDateTime::class.java),
            Arguments.of(SecurityUser::class.java),
        )

        @JvmStatic
        private fun parameterTypes() = listOf(
            Arguments.of(PageRequest::class),
            Arguments.of(PageRequestPeriod::class),
        )

        private const val DEFAULT_PAGE_NUMBER: Long = 1
        private const val DEFAULT_PAGE_SIZE: Long = 10
        private val FORMATTER: DateTimeFormatter = ISO_OFFSET_DATE_TIME
    }

    private val resolver = PageRequestResolver(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)
    private val parameter = mockk<MethodParameter>()
    private val request = mockk<NativeWebRequest>()

    private val pageRequest = PageRequest(
        pageNumber = DEFAULT_PAGE_NUMBER,
        pageSize = DEFAULT_PAGE_SIZE,
        pageSort = listOf(PageSort("qwerty", DESC), PageSort("another", ASC))
    )

    private val pageRequestPeriod = PageRequestPeriod(
        pageNumber = DEFAULT_PAGE_NUMBER,
        pageSize = DEFAULT_PAGE_SIZE,
        pageSort = listOf(PageSort("qwerty", DESC), PageSort("another", ASC))
    )

    private val requestMap = mapOf<KClass<*>, PageRequestData>(
        PageRequest::class to pageRequest,
        PageRequestPeriod::class to pageRequestPeriod,
    )

    private fun withPageNumber(
        klass: KClass<*>,
        pageNumber: Long
    ): PageRequestData = when (klass) {
        PageRequest::class -> {
            pageRequest.copy(pageNumber = pageNumber)
        }
        PageRequestPeriod::class -> {
            pageRequestPeriod.copy(pageNumber = pageNumber)
        }
        else -> throw IllegalArgumentException("Unsupported class: $klass")
    }

    private fun withPageSize(
        klass: KClass<*>,
        pageSize: Long
    ): PageRequestData = when (klass) {
        PageRequest::class -> {
            pageRequest.copy(pageSize = pageSize)
        }
        PageRequestPeriod::class -> {
            pageRequestPeriod.copy(pageSize = pageSize)
        }
        else -> throw IllegalArgumentException("Unsupported class: $klass")
    }

    private fun withPageSort(
        klass: KClass<*>,
        pageSort: List<PageSort>
    ): PageRequestData = when (klass) {
        PageRequest::class -> {
            pageRequest.copy(pageSort = pageSort)
        }
        PageRequestPeriod::class -> {
            pageRequestPeriod.copy(pageSort = pageSort)
        }
        else -> throw IllegalArgumentException("Unsupported class: $klass")
    }

    private fun withPageNumberSize(
        klass: KClass<*>,
        pageNumber: Long,
        pageSize: Long,
    ): PageRequestData = when (klass) {
        PageRequest::class -> {
            pageRequest.copy(pageNumber = pageNumber, pageSize = pageSize)
        }
        PageRequestPeriod::class -> {
            pageRequestPeriod.copy(pageNumber = pageNumber, pageSize = pageSize)
        }
        else -> throw IllegalArgumentException("Unsupported class: $klass")
    }
    private fun withPageNumberSizeSort(
        klass: KClass<*>,
        pageNumber: Long,
        pageSize: Long,
        pageSort: List<PageSort>
    ): PageRequestData = when (klass) {
        PageRequest::class -> {
            pageRequest.copy(pageNumber = pageNumber, pageSize = pageSize, pageSort = pageSort)
        }
        PageRequestPeriod::class -> {
            pageRequestPeriod.copy(pageNumber = pageNumber, pageSize = pageSize, pageSort = pageSort)
        }
        else -> throw IllegalArgumentException("Unsupported class: $klass")
    }

    private fun configureRequest(
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        updatedStart: OffsetDateTime? = null,
        updatedEnd: OffsetDateTime? = null,
        pageNumber: String? = null,
        pageSize: String? = null,
        pageSort: Array<String>? = null,
        parameterType: KClass<*>
    ) {
        clearMocks(request, parameter)

        every { request.getParameter("cs") } returns createdStart?.let { FORMATTER.format(it) }
        every { request.getParameter("ce") } returns createdEnd?.let { FORMATTER.format(it) }
        every { request.getParameter("us") } returns updatedStart?.let { FORMATTER.format(it) }
        every { request.getParameter("ue") } returns updatedEnd?.let { FORMATTER.format(it) }
        every { request.getParameter("p") } returns pageNumber
        every { request.getParameter("s") } returns pageSize
        every { request.getParameterValues("o") } returns pageSort
        every { parameter.parameterType } returns parameterType.java
    }

    private fun PageRequestData.sortParameter() = this.pageSort.map {
        "${it.sortField},${it.sortDirection}"
    }.toTypedArray()

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `Resolver should supports PageRequest class`(klass: KClass<*>) {
        val method = mockk<MethodParameter> {
            every { parameterType } returns klass.java
        }
        assertTrue(resolver.supportsParameter(method))
    }

    @ParameterizedTest
    @MethodSource("classList")
    fun `Resolver should not supports another class besides PageRequest class`(klass: Class<*>) {
        val method = mockk<MethodParameter> {
            every { parameterType } returns klass
        }
        assertFalse(resolver.supportsParameter(method))
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN all parameters are received THEN should return PageRequest with parameters value`(klass: KClass<*>) {
        val expected = withPageNumberSize(
            klass,
            pageNumber = 4,
            pageSize = 15,
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = expected.sortParameter(),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page number parameter is not received THEN should return PageRequest with default page number`(klass: KClass<*>) {
        val expected = withPageSize(klass, 20)
        configureRequest(
            pageSize = expected.pageSize.toString(),
            pageSort = expected.sortParameter(),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page size parameter is not received THEN should return PageRequest with default page size`(klass: KClass<*>) {
        val expected = withPageNumber(
            klass = klass,
            pageNumber = 6,
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSort = expected.sortParameter(),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort parameter is not received THEN should return PageRequest with empty page sort`(klass: KClass<*>) {
        val expected = withPageNumberSizeSort(
            klass = klass,
            pageNumber = 4,
            pageSize = 15,
            pageSort = listOf()
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort field parameter has no trim THEN should return PageRequest with field trimmed`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf(" qwerty "),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort direction parameter is not received THEN should return PageRequest with ASC direction`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty"),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort direction parameter is empty THEN should return PageRequest with ASC direction`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,"),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort direction parameter is blank THEN should return PageRequest with ASC direction`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,  "),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort direction parameter has lowercase THEN should return PageRequest with sort direction`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", DESC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,desc"),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort direction parameter has no trim THEN should return PageRequest with sort direction`(klass: KClass<*>) {
        val expected = withPageSort(
            klass = klass,
            pageSort = listOf(PageSort("qwerty", DESC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty, DESC "),
            parameterType = klass,
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page number parameter is not a number THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageNumber = "t",
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidNumber("p"), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page size parameter is not a number THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSize = "t",
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidNumber("s"), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort parameter has more than 2 values THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSort = arrayOf("qwerty,ASC,DESC"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidSize(), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort parameter is empty THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSort = arrayOf(""),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort parameter is blank THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSort = arrayOf("   "),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort parameter has empty field THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSort = arrayOf(",DESC"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @ParameterizedTest
    @MethodSource("parameterTypes")
    fun `WHEN page sort order parameter has invalid value THEN should throws RequestException`(klass: KClass<*>) {
        configureRequest(
            parameterType = klass,
            pageSort = arrayOf("qwerty,QWERTY"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidSort("QWERTY"), exception.message)
    }

}
