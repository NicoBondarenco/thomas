package com.thomas.core.random

import com.thomas.core.extension.addIfAbsent
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityRole
import java.util.UUID.randomUUID
import kotlin.random.Random

fun randomSecurityRoles(): List<SecurityRole> = Random.nextInt(1, SecurityRole.entries.size).let {
    mutableListOf<SecurityRole>().apply {
        while (this.size < it) {
            this.addIfAbsent(SecurityRole.entries.random())
        }
    }
}

fun randomSecurityGroups(
    quantity: Int = 2
): List<SecurityGroup> = Random.nextInt(1, quantity).let {
    (1..quantity).map {
        SecurityGroup(
            groupId = randomUUID(),
            groupName = "Security Group $it",
            groupRoles = randomSecurityRoles(),
        )
    }
}