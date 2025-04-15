package com.thomas.spring.base.resolver

import com.thomas.core.extension.ISO_OFFSET_DATE_TIME_FORMATTER
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.spring.base.exception.RequestException
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidDate
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidField
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidNumber
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSize
import com.thomas.spring.base.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSort
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

internal class PageRequestResolver(
    private val defaultPageNumber: Long,
    private val defaultPageSize: Long,
) : HandlerMethodArgumentResolver {

    companion object {
        private const val PAGE_NUMBER_PARAM = "p"
        private const val PAGE_SIZE_PARAM = "s"
        private const val SORT_ORDER_PARAM = "o"
        private const val CREATED_START_PARAM = "cs"
        private const val CREATED_END_PARAM = "ce"
        private const val UPDATED_START_PARAM = "us"
        private const val UPDATED_END_PARAM = "ue"

        private val FORMATTER = ISO_OFFSET_DATE_TIME_FORMATTER
    }

    private abstract class PageRequestDataResolver<T : PageRequestData>(
        private val klass: KClass<T>
    ) {
        fun supports(parameter: MethodParameter) = parameter.parameterType == klass.java

        abstract fun resolve(request: NativeWebRequest): T
    }

    private val resolvers = listOf(
        object : PageRequestDataResolver<PageRequest>(PageRequest::class) {
            override fun resolve(
                request: NativeWebRequest
            ): PageRequest = PageRequest(
                request.pageNumber(),
                request.pageSize(),
                request.sortList(),
            )
        },
        object : PageRequestDataResolver<PageRequestPeriod>(PageRequestPeriod::class) {
            override fun resolve(
                request: NativeWebRequest
            ): PageRequestPeriod = PageRequestPeriod(
                request.dateParameter(CREATED_START_PARAM),
                request.dateParameter(CREATED_END_PARAM),
                request.dateParameter(UPDATED_START_PARAM),
                request.dateParameter(UPDATED_END_PARAM),
                request.pageNumber(),
                request.pageSize(),
                request.sortList(),
            )

        },
    )

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return resolvers.any { it.supports(parameter) }
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): PageRequestData = resolvers.first {
        it.supports(parameter)
    }.resolve(webRequest)

    private fun NativeWebRequest.dateParameter(
        attr: String,
    ): OffsetDateTime? = this.getParameter(attr)?.let {
        try {
            OffsetDateTime.parse(it, FORMATTER)
        } catch (e: Exception) {
            throw RequestException(requestPageRequestParameterValidationInvalidDate(it))
        }
    }

    private fun NativeWebRequest.pageNumber() = this.parameterValue(
        PAGE_NUMBER_PARAM,
        defaultPageNumber,
    )

    private fun NativeWebRequest.pageSize() = this.parameterValue(
        PAGE_SIZE_PARAM,
        defaultPageSize,
    )

    private fun NativeWebRequest.parameterValue(
        attr: String,
        default: Long,
    ): Long = this.getParameter(attr)?.let {
        parameterNumber(attr, it)
    } ?: default

    private fun parameterNumber(
        parameter: String,
        value: String,
    ): Long = try {
        value.toLong()
    } catch (e: NumberFormatException) {
        throw RequestException(requestPageRequestParameterValidationInvalidNumber(parameter))
    }

    private fun NativeWebRequest.sortList() = this.getParameterValues(
        SORT_ORDER_PARAM
    )?.map {
        handleSortParameter(it)
    } ?: listOf()

    private fun handleSortParameter(
        parameter: String
    ): PageSort = parameter.toSortParameters().let {
        PageSort(it.toSortFieldParameter(), it.toSortDirectionParameter())
    }

    private fun String.toSortParameters() = this.split(",").takeIf {
        it.size <= 2
    } ?: throw RequestException(requestPageRequestParameterValidationInvalidSize())

    private fun List<String>.toSortFieldParameter() =
        this.first().toSortFieldParameter()

    private fun String.toSortFieldParameter(): String = this.trim().takeIf {
        it.isNotEmpty()
    } ?: throw RequestException(requestPageRequestParameterValidationInvalidField())

    private fun List<String>.toSortDirectionParameter() = this.takeIf {
        it.size > 1
    }?.lastOrNull().toSortDirectionParameter()

    private fun String?.toSortDirectionParameter(): PageSortDirection = this?.trim()?.takeIf {
        it.isNotEmpty()
    }?.toPageSortDirection() ?: PageSortDirection.ASC

    private fun String.toPageSortDirection() = PageSortDirection.entries.firstOrNull {
        it.name == this.trim().uppercase()
    } ?: throw RequestException(requestPageRequestParameterValidationInvalidSort(this))

}
