package com.thomas.spring.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.authentication.Authenticator
import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.spring.extension.toExceptionResponse
import com.thomas.spring.filter.AuthenticationFilter
import com.thomas.spring.i18n.SpringMessageI18N.requestFilterChainAuthenticationEntrypointAccessDenied
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.NEVER
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    @Bean
    fun restAuthenticationEntryPoint(
        objectMapper: ObjectMapper
    ): AuthenticationEntryPoint = AuthenticationEntryPoint { request, response, _ ->
        val exception = UnauthorizedUserException(requestFilterChainAuthenticationEntrypointAccessDenied())
        response.contentType = APPLICATION_JSON_VALUE
        response.status = UNAUTHORIZED.value()
        response.writer.write(objectMapper.writeValueAsString(exception.toExceptionResponse(request.requestURI)))
    }

    @Bean
    fun authenticationFilter(
        authenticator: Authenticator
    ): AuthenticationFilter = AuthenticationFilter(authenticator)

    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationFilter: AuthenticationFilter,
        restAuthenticationEntryPoint: AuthenticationEntryPoint
    ): SecurityFilterChain = http.cors {
        it.disable()
    }.sessionManagement {
        it.sessionCreationPolicy(NEVER)
    }.csrf {
        it.disable()
    }.formLogin {
        it.disable()
    }.httpBasic {
        it.disable()
    }.exceptionHandling {
        it.authenticationEntryPoint(restAuthenticationEntryPoint)
    }.authorizeHttpRequests {
        it.requestMatchers("/public/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/configuration/**").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/swagger-ui.html").permitAll()
            .requestMatchers("/webjars/**").permitAll()
            .anyRequest().authenticated()
    }.addFilterBefore(
        authenticationFilter,
        UsernamePasswordAuthenticationFilter::class.java
    ).build()

}
