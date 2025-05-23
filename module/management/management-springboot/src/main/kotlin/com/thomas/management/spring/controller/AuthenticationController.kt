package com.thomas.management.spring.controller

import com.thomas.management.domain.AuthenticationService
import com.thomas.management.domain.model.request.LoginRequest
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.domain.model.response.AccessTokenResponse
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_AUTHENTICATION
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_AUTHENTICATION_LOGIN
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_AUTHENTICATION_REFRESH
import com.thomas.spring.base.mapping.PostMappingJson
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PUBLIC_API_V1_AUTHENTICATION)
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMappingJson([PUBLIC_API_V1_AUTHENTICATION_LOGIN])
    suspend fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<AccessTokenResponse> = ResponseEntity.ok(authenticationService.login(request))

    @PostMappingJson([PUBLIC_API_V1_AUTHENTICATION_REFRESH])
    suspend fun refresh(
        @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<AccessTokenResponse> = ResponseEntity.ok(authenticationService.refresh(request))

}
