package com.thomas.management.spring.configuration

import com.thomas.management.domain.properties.PasswordProperties
import com.thomas.management.domain.properties.SignupProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PropertiesConfiguration {

    @Bean
    fun signupProperties(
        @Value("\${signup.enabled}") signupEnabled: Boolean
    ): SignupProperties = SignupProperties(signupEnabled)

    @Bean
    fun passwordProperties(
        @Value("\${passwordReset.tokenValidityMinutes}") tokenValidityMinutes: Long,
        @Value("\${passwordReset.resetEmailSubject}") resetEmailSubject: String,
        @Value("\${passwordReset.resetEmailModel}") resetEmailModel: String,
        @Value("\${passwordReset.tokenValidityPattern}") tokenValidityPattern: String,
    ): PasswordProperties = PasswordProperties(
        tokenValidityMinutes = tokenValidityMinutes,
        resetEmailSubject = resetEmailSubject,
        resetEmailModel = resetEmailModel,
        tokenValidityPattern = tokenValidityPattern,
    )

}
