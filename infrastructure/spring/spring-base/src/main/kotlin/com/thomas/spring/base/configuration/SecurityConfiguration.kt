package com.thomas.spring.base.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.spring.base.authentication.ApplicationAuthorizationManager
import com.thomas.spring.base.authentication.Authenticator
import com.thomas.spring.base.authentication.JWTAuth0Authenticator
import com.thomas.spring.base.extension.toExceptionResponse
import com.thomas.spring.base.authentication.AuthenticationFilter
import com.thomas.spring.base.i18n.SpringMessageI18N.requestFilterChainAuthenticationEntrypointAccessDenied
import com.thomas.spring.base.properties.JWTProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JWTProperties::class)
open class SecurityConfiguration {

    @Bean
    open fun restAuthenticationEntryPoint(
        objectMapper: ObjectMapper
    ): AuthenticationEntryPoint = AuthenticationEntryPoint { request, response, _ ->
        val exception = UnauthorizedUserException(requestFilterChainAuthenticationEntrypointAccessDenied())
        response.contentType = APPLICATION_JSON_VALUE
        response.status = UNAUTHORIZED.value()
        response.writer.write(objectMapper.writeValueAsString(exception.toExceptionResponse(request.requestURI)))
    }

    @Bean
    open fun authenticator(
        configuration: JWTProperties,
        objectMapper: ObjectMapper
    ): Authenticator = JWTAuth0Authenticator(configuration, objectMapper)

    @Bean
    open fun authenticationFilter(
        authenticator: Authenticator
    ): AuthenticationFilter = AuthenticationFilter(authenticator)

    @Bean
    open fun filterChain(
        http: HttpSecurity,
        authenticationFilter: AuthenticationFilter,
        restAuthenticationEntryPoint: AuthenticationEntryPoint,
        authorizationManager: ApplicationAuthorizationManager
    ): SecurityFilterChain = http.cors {
        it.disable()
    }.sessionManagement {
        it.disable()
    }.csrf {
        it.disable()
    }.formLogin {
        it.disable()
    }.httpBasic {
        it.disable()
    }.anonymous {
        it.disable()
    }.exceptionHandling {
        it.authenticationEntryPoint(restAuthenticationEntryPoint)
    }.authorizeHttpRequests {
        it.requestMatchers("/public/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/configuration/**").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/swagger-resources").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/swagger-ui.html").permitAll()
            .requestMatchers("/webjars/**").permitAll()
            .anyRequest().access(authorizationManager)
    }.addFilterBefore(
        authenticationFilter,
        UsernamePasswordAuthenticationFilter::class.java
    ).build()

}
