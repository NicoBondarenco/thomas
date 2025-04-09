package com.thomas.spring.base.configuration

import com.thomas.spring.base.properties.PaginationDefaultProperties
import com.thomas.spring.base.properties.PaginationProperties
import com.thomas.spring.base.resolver.PageRequestResolver
import com.thomas.spring.base.resolver.RequestLocaleResolver
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(PaginationProperties::class)
open class ResolverConfiguration {

    @Configuration
    open class ParameterResolverConfigurer(
        private val paginationProperties: PaginationProperties
    ) : WebMvcConfigurer {

        override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
            resolvers.add(paginationProperties.toPageRequestResolver())
        }

        private fun PaginationProperties.toPageRequestResolver() =
            this.default.toPageRequestResolver()

        private fun PaginationDefaultProperties.toPageRequestResolver() =
            PageRequestResolver(this.pageNumber, this.pageSize)

    }

    @Bean
    open fun localeResolver(): LocaleResolver = RequestLocaleResolver()

}
