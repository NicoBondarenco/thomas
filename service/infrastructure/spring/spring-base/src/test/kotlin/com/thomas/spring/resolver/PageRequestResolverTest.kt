package com.thomas.spring.resolver

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.exception.RequestException
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidField
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidNumber
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSize
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSort
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import java.time.OffsetDateTime
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

        private const val DEFAULT_PAGE_NUMBER: Long = 1
        private const val DEFAULT_PAGE_SIZE: Long = 10
    }

    private val resolver = PageRequestResolver(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)
    private val parameter = mockk<MethodParameter>()
    private val request = mockk<NativeWebRequest>()

    private val pageRequest = PageRequest(
        pageNumber = DEFAULT_PAGE_NUMBER,
        pageSize = DEFAULT_PAGE_SIZE,
        pageSort = listOf(PageSort("qwerty", DESC), PageSort("another", ASC))
    )

    private fun configureRequest(
        pageNumber: String? = null,
        pageSize: String? = null,
        pageSort: Array<String>? = null,
    ) {
        clearMocks(request)

        every { request.getParameter("p") } returns pageNumber
        every { request.getParameter("s") } returns pageSize
        every { request.getParameterValues("o") } returns pageSort
    }

    private fun PageRequest.sortParameter() = this.pageSort.map {
        "${it.sortField},${it.sortDirection}"
    }.toTypedArray()

    @Test
    fun `Resolver should supports PageRequest class`() {
        val method = mockk<MethodParameter> {
            every { parameterType } returns PageRequest::class.java
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

    @Test
    fun `WHEN all parameters are received THEN should return PageRequest with parameters value`() {
        val expected = pageRequest.copy(
            pageNumber = 4,
            pageSize = 15,
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = expected.sortParameter(),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page number parameter is not received THEN should return PageRequest with default page number`() {
        val expected = pageRequest.copy(
            pageSize = 20,
        )
        configureRequest(
            pageSize = expected.pageSize.toString(),
            pageSort = expected.sortParameter(),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page size parameter is not received THEN should return PageRequest with default page size`() {
        val expected = pageRequest.copy(
            pageNumber = 6,
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSort = expected.sortParameter(),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort parameter is not received THEN should return PageRequest with empty page sort`() {
        val expected = pageRequest.copy(
            pageNumber = 4,
            pageSize = 15,
            pageSort = listOf()
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort field parameter has no trim THEN should return PageRequest with field trimmed`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf(" qwerty "),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort direction parameter is not received THEN should return PageRequest with ASC direction`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty"),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort direction parameter is empty THEN should return PageRequest with ASC direction`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,"),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort direction parameter is blank THEN should return PageRequest with ASC direction`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", ASC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,  "),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort direction parameter has lowercase THEN should return PageRequest with sort direction`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", DESC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty,desc"),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page sort direction parameter has no trim THEN should return PageRequest with sort direction`() {
        val expected = pageRequest.copy(
            pageSort = listOf(PageSort("qwerty", DESC))
        )
        configureRequest(
            pageNumber = expected.pageNumber.toString(),
            pageSize = expected.pageSize.toString(),
            pageSort = arrayOf("qwerty, DESC "),
        )

        val pageable = resolver.resolveArgument(parameter, null, request, null)

        assertEquals(expected, pageable)
    }

    @Test
    fun `WHEN page number parameter is not a number THEN should throws RequestException`() {
        configureRequest(
            pageNumber = "t",
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidNumber("p"), exception.message)
    }

    @Test
    fun `WHEN page size parameter is not a number THEN should throws RequestException`() {
        configureRequest(
            pageSize = "t",
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidNumber("s"), exception.message)
    }

    @Test
    fun `WHEN page sort parameter has more than 2 values THEN should throws RequestException`() {
        configureRequest(
            pageSort = arrayOf("qwerty,ASC,DESC"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidSize(), exception.message)
    }

    @Test
    fun `WHEN page sort parameter is empty THEN should throws RequestException`() {
        configureRequest(
            pageSort = arrayOf(""),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @Test
    fun `WHEN page sort parameter is blank THEN should throws RequestException`() {
        configureRequest(
            pageSort = arrayOf("   "),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @Test
    fun `WHEN page sort parameter has empty field THEN should throws RequestException`() {
        configureRequest(
            pageSort = arrayOf(",DESC"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidField(), exception.message)
    }

    @Test
    fun `WHEN page sort order parameter has invalid value THEN should throws RequestException`() {
        configureRequest(
            pageSort = arrayOf("qwerty,QWERTY"),
        )

        val exception = assertThrows<RequestException> {
            resolver.resolveArgument(parameter, null, request, null)
        }

        assertEquals(requestPageRequestParameterValidationInvalidSort("QWERTY"), exception.message)
    }

}
