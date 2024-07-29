package com.thomas.hash

interface Hasher {

    fun hash(value: String, salt: String): String

    fun generateSalt(): String

}