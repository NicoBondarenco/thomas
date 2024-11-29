package com.thomas.management.domain

import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest
import com.thomas.management.domain.model.response.UserSimpleResponse

interface PasswordService {

    suspend fun changePassword(request: ChangePasswordRequest): UserSimpleResponse

    suspend fun forgotPassword(request: ForgotPasswordRequest)

    suspend fun resetPassword(request: ResetPasswordRequest)

}