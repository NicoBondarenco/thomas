package com.thomas.authentication.domain

import com.thomas.authentication.domain.model.request.ChangePasswordRequest
import com.thomas.authentication.domain.model.request.ForgotPasswordRequest
import com.thomas.authentication.domain.model.request.PasswordResetRequest

interface PasswordService {

    fun forgotPassword(request: ForgotPasswordRequest)

    fun resetPassword(request: PasswordResetRequest)

    fun changePassword(request: ChangePasswordRequest)

}
