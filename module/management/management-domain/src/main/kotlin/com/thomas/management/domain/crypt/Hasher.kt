package com.thomas.management.domain.crypt

interface Hasher {

    suspend fun hash(value: String, salt: String): String

    suspend fun generateSalt(): String

}