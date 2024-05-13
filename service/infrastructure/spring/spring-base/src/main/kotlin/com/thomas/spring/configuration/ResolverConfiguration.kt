package com.thomas.spring.configuration

import com.thomas.spring.properties.PaginationProperties
import com.thomas.spring.resolver.PageRequestResolver
import com.thomas.spring.resolver.RequestLocaleResolver
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
class ResolverConfiguration {

    @Configuration
    class ParameterResolverConfigurer(
        private val paginationProperties: PaginationProperties
    ) : WebMvcConfigurer {

        override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
            resolvers.add(
                PageRequestResolver(
                    paginationProperties.default.pageNumber,
                    paginationProperties.default.pageSize
                )
            )
        }

    }

    @Bean
    fun localeResolver(): LocaleResolver = RequestLocaleResolver()

}
