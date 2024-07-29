package com.thomas.hash.bouncycastle

import com.thomas.hash.Hasher
import com.thomas.hash.bouncycastle.exception.BCryptHasherException.Companion.throwInvalidSalt
import com.thomas.hash.bouncycastle.properties.BCryptProperties
import com.thomas.hash.bouncycastle.properties.BCryptProperties.Companion.SALT_BYTE_SIZE
import java.util.Base64
import org.bouncycastle.crypto.generators.BCrypt

class BCryptHasher(
    private val properties: BCryptProperties
) : BouncyCastleHasher(SALT_BYTE_SIZE) {

    override fun hash(
        value: String,
        salt: String
    ): String = salt.toByteArray().let { saltArray ->
        saltArray.takeIf {
            it.size == SALT_BYTE_SIZE
        }?.let {
            val salted = BCrypt.generate(value.toByteArray(), it, properties.costValue)
            val hashed = BCrypt.generate(salted, properties.pepperHash.toByteArray(), properties.costValue)
            Base64.getEncoder().encodeToString(hashed)
        } ?: throwInvalidSalt("Salt", saltArray.size)
    }

}
