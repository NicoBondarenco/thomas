package com.thomas.authentication.jwt.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.thomas.authentication.Authenticator
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0Configuration
import com.thomas.authentication.jwt.auth0.exception.JWTAuth0TokenException
import com.thomas.authentication.jwt.auth0.extension.toSecurityUser
import com.thomas.authentication.jwt.auth0.extension.verifier
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveClaimInvalidId
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveClaimMissingId
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveUserInactiveUser
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveUserNotFound
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenDecodeError
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenExpiredToken
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenInvalidIssuer
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenInvalidSignature
import com.thomas.authentication.jwt.auth0.repository.UserAuthenticationRepository
import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.model.security.SecurityUser
import java.util.UUID

class JWTAuth0Authenticator(
    configuration: JWTAuth0Configuration,
    private val repository: UserAuthenticationRepository
) : Authenticator {

    private val verifier = configuration.verifier()
    private val decoder = JWT()

    override fun authenticate(
        token: String
    ): SecurityUser = try {
        verifier.verify(token).activeSecurityUser()
    } catch (e: TokenExpiredException) {
        throw JWTAuth0TokenException(authenticationTokenValidateTokenExpiredToken(), e)
    } catch (e: JWTDecodeException) {
        throw JWTAuth0TokenException(authenticationTokenValidateTokenDecodeError(), e)
    } catch (e: SignatureVerificationException) {
        throw JWTAuth0TokenException(authenticationTokenValidateTokenInvalidSignature(), e)
    } catch (e: IncorrectClaimException) {
        throw JWTAuth0TokenException(authenticationTokenValidateTokenInvalidIssuer(), e)
    }

    override fun decode(
        token: String,
    ): SecurityUser = decoder.decodeJwt(token).securityUser()

    private fun DecodedJWT.activeSecurityUser(): SecurityUser = securityUser()
        .takeIf { it.isActive }
        ?: throw UnauthorizedUserException(authenticationTokenRetrieveUserInactiveUser())

    private fun DecodedJWT.securityUser() = repository.findUserAuthentication(userId())
        ?.toSecurityUser()
        ?: throw UnauthorizedUserException(authenticationTokenRetrieveUserNotFound())

    private fun DecodedJWT.userId() = this.claims[SecurityUser::userId.name]?.asUUID()
        ?: throw JWTAuth0TokenException(authenticationTokenRetrieveClaimMissingId())

    private fun Claim.asUUID() = try {
        UUID.fromString(this.asString())
    } catch (e: IllegalArgumentException) {
        throw JWTAuth0TokenException(authenticationTokenRetrieveClaimInvalidId(), e)
    }

}
