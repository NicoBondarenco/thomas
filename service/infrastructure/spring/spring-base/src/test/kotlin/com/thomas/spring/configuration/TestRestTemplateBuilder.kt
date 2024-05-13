package com.thomas.spring.configuration

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component

@Component
class TestRestTemplateBuilder(
    private val restTemplateBuilder: RestTemplateBuilder,
    private val mappingJacksonHttpMessageConverter: MappingJackson2HttpMessageConverter,
) {

    fun testRestTemplate(
        port: Int,
        token: String? = null,
        bearer: String? = "Bearer",
        locale: String? = "pt-BR",
    ): TestRestTemplate {
        var builder = restTemplateBuilder
            .additionalMessageConverters(mappingJacksonHttpMessageConverter)
            .rootUri("http://localhost:$port/")

        token?.apply {
            builder = builder.defaultHeader(AUTHORIZATION, "$bearer $this")
        }

        locale?.apply {
            builder = builder.defaultHeader(ACCEPT_LANGUAGE, this)
        }

        return TestRestTemplate(builder)
    }

}