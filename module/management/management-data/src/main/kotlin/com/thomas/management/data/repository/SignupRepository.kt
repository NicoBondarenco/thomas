package com.thomas.management.data.repository

import com.thomas.management.data.entity.SignupEntity

interface SignupRepository {

    suspend fun signup(entity: SignupEntity): SignupEntity

}