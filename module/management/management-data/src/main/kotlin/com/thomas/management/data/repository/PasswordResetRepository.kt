package com.thomas.management.data.repository

import com.thomas.management.data.entity.PasswordResetEntity

interface PasswordResetRepository {

    suspend fun upsertToken(entity: PasswordResetEntity): PasswordResetEntity

    suspend fun findByToken(resetToken: String): PasswordResetEntity?

}
