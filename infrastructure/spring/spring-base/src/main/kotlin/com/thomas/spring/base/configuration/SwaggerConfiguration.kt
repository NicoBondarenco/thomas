package com.thomas.spring.base.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SwaggerConfiguration {

    @Bean
    open fun openApi(): OpenAPI = OpenAPI()
        .info(Info().title("T.H.O.M.A.S. Project").version("latest"))
        .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
        .components(
            Components().addSecuritySchemes(
                "bearerAuth",
                SecurityScheme().type(HTTP).scheme("bearer").bearerFormat("JWT")
            )
        )

    @Bean
    open fun authenticatedApiV1(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("authenticated - v1")
        .pathsToMatch("/api/v1/**")
        .build()

    @Bean
    open fun publicApiV1(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("public - v1")
        .pathsToMatch("/public/api/v1/**")
        .build()

}
