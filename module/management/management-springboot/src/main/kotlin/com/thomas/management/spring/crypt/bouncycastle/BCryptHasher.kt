package com.thomas.management.spring.crypt.bouncycastle

import com.thomas.core.extension.addIf
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.spring.configuration.properties.BCryptProperties
import com.thomas.management.spring.configuration.properties.BCryptProperties.Companion.SALT_BYTE_SIZE
import com.thomas.management.spring.crypt.bouncycastle.BCryptHasherException.Companion.throwInvalidSalt
import java.util.Base64
import org.bouncycastle.crypto.generators.BCrypt

class BCryptHasher(
    private val properties: BCryptProperties
) : Hasher {

    companion object {
        private val SALT_CHAR: List<String> = listOf(
            "0123456789",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "\"'!¹@²#³$£%¢¨¬&*()_+-=§",
            "`´[]{}ªº^~,.<>;:/?|\\",
            "aàáâãäạảăắặằẳẵấậầẩẫåāą",
            "cçčćĉċdđðďeèéêëẹẻẽếệềểễēĕėęě",
            "gĝğġģhĥħiìíîïịỉĩīĭį̇ıjĵkķĸ",
            "lĺļľŀłnñńņňŉŋoòóôõöọỏốộồổỗơớợờởỡøōŏő",
            "ŕŗřsšśŝştţťŧuùúûüụủũưứựừửữŭūůűų",
            "wŵyýÿỵỳỷỹŷzžźż",
            "AÀÁÂÃÄẠẢĂẮẶẰẲẴẤẬẦẨẪÅĀĄ",
            "CÇČĆĈĊDĐÐĎEÈÉÊËẸẺẼẾỆỀỂỄĒĔĖĘĚ",
            "GĜĞĠĢHĤĦIÌÍÎÏỊỈĨĪĬĮ̇IJĴKĶĸ",
            "LĹĻĽĿŁNÑŃŅŇNŊOÒÓÔÕÖỌỎỐỘỒỔỖƠỚỢỜỞỠØŌŎŐ",
            "ŔŖŘSŠŚŜŞTŢŤŦUÙÚÛÜỤỦŨƯỨỰỪỬỮŬŪŮŰŲ",
            "WŴYÝŸỴỲỶỸŶZŽŹŻ",
        ).joinToString("").map { it.toString() }
    }

    override suspend fun hash(
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

    override suspend fun generateSalt(): String {
        val array = mutableListOf<String>()
        while (array.totalBytes() < SALT_BYTE_SIZE) {
            array.addArray(SALT_CHAR.random())
        }
        return array.joinToString(separator = "")
    }

    private fun MutableList<String>.totalBytes() = this.sumOf { it.toByteArray().size }

    private fun MutableList<String>.canAdd(
        element: String
    ): Boolean = this.totalBytes() + element.toByteArray().size <= SALT_BYTE_SIZE

    private fun MutableList<String>.addArray(
        element: String
    ) = this.addIf(element) { this.canAdd(it) }

}
