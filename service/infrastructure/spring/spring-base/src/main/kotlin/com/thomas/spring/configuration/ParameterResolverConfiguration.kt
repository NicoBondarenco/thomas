package com.thomas.spring.configuration

import com.thomas.spring.resolver.PageRequestResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class ParameterResolverConfiguration(
    @Value("\${pageable.default.pageNumber:1}") private val defaultPageNumber: Long,
    @Value("\${pageable.default.pageSize:20}") private val defaultPageSize: Long,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(PageRequestResolver(defaultPageNumber, defaultPageSize))
    }

}