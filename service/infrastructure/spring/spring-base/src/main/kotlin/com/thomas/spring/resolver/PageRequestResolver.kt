package com.thomas.spring.resolver

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.spring.exception.RequestException
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidField
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidNumber
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSize
import com.thomas.spring.i18n.SpringMessageI18N.requestPageRequestParameterValidationInvalidSort
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class PageRequestResolver(
    private val defaultPageNumber: Long,
    private val defaultPageSize: Long,
) : HandlerMethodArgumentResolver {

    companion object {
        private const val PAGE_NUMBER_PARAM = "p"
        private const val PAGE_SIZE_PARAM = "s"
        private const val SORT_ORDER_PARAM = "o"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == PageRequest::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): PageRequest = PageRequest(
        webRequest.pageNumber(),
        webRequest.pageSize(),
        webRequest.sortList(),
    )

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
