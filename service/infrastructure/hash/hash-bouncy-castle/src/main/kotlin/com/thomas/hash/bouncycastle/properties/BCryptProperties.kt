package com.thomas.hash.bouncycastle.properties

import com.thomas.hash.bouncycastle.exception.BCryptHasherException.Companion.throwInvalidCost
import com.thomas.hash.bouncycastle.exception.BCryptHasherException.Companion.throwInvalidSalt

data class BCryptProperties(
    val pepperHash: String,
    val costValue: Int = 4,
) {

    companion object {
        internal const val SALT_BYTE_SIZE = 16
        internal const val COST_VALUE_MIN = 4
        internal const val COST_VALUE_MAX = 31
    }

    init {
        pepperHash.toByteArray().size.apply {
            this.takeIf { it == SALT_BYTE_SIZE } ?: throwInvalidSalt("Pepper", this)
        }

        costValue.takeIf {
            it in COST_VALUE_MIN..COST_VALUE_MAX
        } ?: throwInvalidCost(
            COST_VALUE_MIN,
            COST_VALUE_MAX,
            costValue
        )
    }

}
