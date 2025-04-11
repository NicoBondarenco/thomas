package com.thomas.management.spring.configuration.properties

import com.thomas.management.spring.crypt.bouncycastle.BCryptHasherException.Companion.throwInvalidCost
import com.thomas.management.spring.crypt.bouncycastle.BCryptHasherException.Companion.throwInvalidSalt
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bcrypt")
data class BCryptProperties(
    val pepperHash: String = "",
    val costValue: Int = 0,
) {

    companion object {
        const val SALT_BYTE_SIZE = 16
        const val COST_VALUE_MIN = 4
        const val COST_VALUE_MAX = 31
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
