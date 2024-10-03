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
): List<SecurityGroup> = (1..quantity).map {
    randomSecurityGroup()
}

fun randomSecurityGroup() = SecurityGroup(
    groupId = randomUUID(),
    groupName = "Security Group ${randomString(5)}",
    groupRoles = randomSecurityRoles(),
)
