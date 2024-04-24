package com.thomas.jwt.auth0.util

fun Any.readResourceText(
    path: String
) = this.javaClass
    .classLoader
    .getResource(path)!!
    .readText()
