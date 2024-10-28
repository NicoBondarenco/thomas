package com.thomas.hasher

interface Hasher {

    suspend fun hash(value: String, salt: String): String

    suspend fun generateSalt(): String

}