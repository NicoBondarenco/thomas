package com.thomas.hash.bouncycastle

import com.thomas.core.extension.addIf
import com.thomas.hash.Hasher
import com.thomas.hash.bouncycastle.properties.SALT_CHAR

abstract class BouncyCastleHasher(
    private val saltSize: Int,
) : Hasher {

    override fun generateSalt(): String {
        val array = mutableListOf<String>()
        while (array.totalBytes() < saltSize) {
            array.addArray(SALT_CHAR.random())
        }
        return array.joinToString(separator = "")
    }

    private fun MutableList<String>.totalBytes() = this.sumOf { it.toByteArray().size }

    private fun MutableList<String>.canAdd(
        element: String
    ): Boolean = this.totalBytes() + element.toByteArray().size <= saltSize

    private fun MutableList<String>.addArray(
        element: String
    ) = this.addIf(element) { this.canAdd(it) }

}
