package com.thomas.hash.bouncycastle.properties

data class Argon2Properties(
    val pepperHash: String,
    val iterationsCount: Int = 2,
    val memoryLimit: Int = 33268,
    val hashLength: Int = 64,
    val parallelismCount: Int = 1,
)
