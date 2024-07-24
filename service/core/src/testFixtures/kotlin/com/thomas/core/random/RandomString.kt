package com.thomas.core.random

private val CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toList()

fun randomString(
    length: Int
): String = (1..length).map {
    CHARS.shuffled().first()
}.joinToString("")
