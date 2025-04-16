package com.thomas.management.spring.controller

import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_PASSWORD
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_PASSWORD_FORGOT
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_PASSWORD_RESET
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PUBLIC_API_V1_PASSWORD)
class PasswordController(
    private val passwordService: PasswordService,
) {

    @PostMapping(PUBLIC_API_V1_PASSWORD_FORGOT)
    suspend fun forgotPassword(
        @RequestBody request: ForgotPasswordRequest
    ): ResponseEntity<Unit> = passwordService.forgotPassword(request).let {
        noContent().build()
    }

    @PatchMapping(PUBLIC_API_V1_PASSWORD_RESET)
    suspend fun resetPassword(
        @RequestBody request: ResetPasswordRequest
    ): ResponseEntity<Unit> = passwordService.resetPassword(request).let {
        accepted().build()
    }

}
