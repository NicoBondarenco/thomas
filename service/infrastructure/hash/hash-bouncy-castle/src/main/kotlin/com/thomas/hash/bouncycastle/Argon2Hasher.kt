package com.thomas.hash.bouncycastle

import com.thomas.hash.bouncycastle.properties.Argon2Properties
import java.util.Base64
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import org.bouncycastle.crypto.params.Argon2Parameters.ARGON2_VERSION_13

class Argon2Hasher(
    properties: Argon2Properties
) : BouncyCastleHasher(properties.hashLength) {

    private val length: Int = properties.hashLength

    private val parameters: Argon2Parameters = Argon2Parameters.Builder()
        .withVersion(ARGON2_VERSION_13)
        .withIterations(properties.iterationsCount)
        .withMemoryAsKB(properties.memoryLimit)
        .withParallelism(properties.parallelismCount)
        .withSalt(properties.pepperHash.toByteArray())
        .build()

    private val generator: Argon2BytesGenerator = Argon2BytesGenerator().also {
        it.init(parameters)
    }

    private fun saltParameters(salt: String) = Argon2Parameters.Builder()
        .withVersion(ARGON2_VERSION_13)
        .withIterations(parameters.iterations)
        .withMemoryAsKB(parameters.memory)
        .withParallelism(parameters.lanes)
        .withSalt(salt.toByteArray())
        .build()

    private fun saltGenerator(salt: String) = Argon2BytesGenerator().also {
        it.init(saltParameters(salt))
    }


    override fun hash(value: String, salt: String): String {
        val salted = ByteArray(length).also {
            saltGenerator(salt).generateBytes(value.toByteArray(), it)
        }
        val hashed = ByteArray(length).also {
            generator.generateBytes(salted, it)
        }
        return Base64.getEncoder().encodeToString(hashed)
    }

}
