package com.thomas.hash.bouncycastle.exception

class BCryptHasherException(message: String): Exception(message) {

    companion object {
        internal fun throwInvalidSalt(
            field: String,
            size: Int,
        ): Nothing = throw BCryptHasherException("$field hash length must be 16 bytes, got $size")

        internal fun throwInvalidCost(
            min: Int,
            max: Int,
            cost: Int,
        ): Nothing = throw BCryptHasherException("Cost value must be between $min and $max, got $cost")
    }

}