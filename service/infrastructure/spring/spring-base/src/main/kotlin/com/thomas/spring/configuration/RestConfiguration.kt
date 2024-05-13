package com.thomas.spring.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
class RestConfiguration {

    @Bean
    fun restTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        mappingJacksonHttpMessageConverter: MappingJackson2HttpMessageConverter
    ): RestTemplate = restTemplateBuilder
        .additionalMessageConverters(mappingJacksonHttpMessageConverter)
        .build()

}
