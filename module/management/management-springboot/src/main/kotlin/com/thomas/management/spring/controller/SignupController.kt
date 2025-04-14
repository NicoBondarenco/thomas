package com.thomas.management.spring.controller

import com.thomas.management.domain.SignupService
import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.model.response.SignupResponse
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_SIGNUP
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PUBLIC_API_V1_SIGNUP)
class SignupController(
    private val signupService: SignupService
) {

    @PostMapping
    suspend fun signup(
        @RequestBody signupRequest: SignupRequest
    ): ResponseEntity<SignupResponse> = ResponseEntity.ok(signupService.signup(signupRequest))

}