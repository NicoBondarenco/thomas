package com.thomas.spring.base.authenticator

import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.base.exception.JWTTokenException
import com.thomas.spring.base.extension.verifier
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenRetrieveUserInactiveUser
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenDecodeError
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenExpiredToken
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenInvalidIssuer
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenInvalidSignature
import com.thomas.spring.base.properties.JWTProperties
import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64

class JWTAuth0Authenticator internal constructor(
    configuration: JWTProperties,
    private val mapper: ObjectMapper
) : Authenticator {

    private val verifier = configuration.verifier()

    override fun authenticate(
        token: String
    ): SecurityUser = try {
        verifier.verify(token).securityUser()
    } catch (e: TokenExpiredException) {
        throw JWTTokenException(authenticationTokenValidateTokenExpiredToken(), e)
    } catch (e: JWTDecodeException) {
        throw JWTTokenException(authenticationTokenValidateTokenDecodeError(), e)
    } catch (e: SignatureVerificationException) {
        throw JWTTokenException(authenticationTokenValidateTokenInvalidSignature(), e)
    } catch (e: IncorrectClaimException) {
        throw JWTTokenException(authenticationTokenValidateTokenInvalidIssuer(), e)
    }

    private fun DecodedJWT.securityUser() = this.payload.let {
        val decoded = Base64.getDecoder().decode(it)
        val content = String(decoded, UTF_8)
        mapper.readValue<SecurityUser>(content)
    }.takeIf { it.isActive }
        ?: throw UnauthorizedUserException(authenticationTokenRetrieveUserInactiveUser())

}
