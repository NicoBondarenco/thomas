package com.thomas.authentication.data.repository

import com.thomas.authentication.data.entity.PasswordResetCompleteEntity
import com.thomas.authentication.data.entity.PasswordResetEntity

interface ResetPasswordRepository {

    fun findByToken(token: String): PasswordResetCompleteEntity?

    fun upsert(entity: PasswordResetEntity): PasswordResetEntity

}
