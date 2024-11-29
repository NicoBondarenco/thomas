package com.thomas.management.data.repository

import com.thomas.management.data.entity.PasswordResetEntity

interface PasswordResetRepository {

    suspend fun upsertToken(entity: PasswordResetEntity)

}
