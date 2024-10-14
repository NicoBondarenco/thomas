package com.thomas.core.generator

object StringGenerator {

    private val CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toList()

    fun randomString(
        length: Int = 10
    ): String = (1..length).map {
        CHARS.shuffled().first()
    }.joinToString("")


}