package com.thomas.spring.configuration

import com.thomas.spring.properties.PaginationProperties
import com.thomas.spring.resolver.PageRequestResolver
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class ResolverConfiguration{

    @Bean
    @ConfigurationProperties("pageable")
    fun paginationProperties() = PaginationProperties()

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

}
