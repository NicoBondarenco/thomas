package com.thomas.core.model.security

import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_DELETE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SecurityUserTest {

    private val user = SecurityUser(
        randomUUID(),
        "Security",
        "User",
        "security.user@test.com",
        "16988776655",
        null,
        LocalDate.now(ZoneOffset.UTC),
        Gender.CIS_MALE,
        UserProfile.ADMINISTRATOR,
        true,
        listOf(),
        listOf(),
    )

    private val masterUser = user.copy(
        userRoles = listOf(SecurityRole.MASTER)
    )

    private val masterGroup = user.copy(
        userGroups = listOf(
            SecurityGroup(
                groupId = randomUUID(),
                groupName = "Security Group",
                groupRoles = listOf(SecurityRole.MASTER),
            )
        )
    )

    private val commonUser = user.copy(
        userRoles = listOf(ROLE_GROUP_READ)
    )

    private val commonGroup = user.copy(
        userGroups = listOf(
            SecurityGroup(
                groupId = randomUUID(),
                groupName = "Security Group",
                groupRoles = listOf(ROLE_GROUP_READ),
            )
        )
    )

    private val rolesUser = user.copy(
        userRoles = listOf(
            ROLE_GROUP_READ,
            ROLE_GROUP_CREATE,
            ROLE_GROUP_UPDATE,
            ROLE_GROUP_DELETE,
        ),
        userGroups = listOf(
            SecurityGroup(
                groupId = randomUUID(),
                groupName = "Security Group",
                groupRoles = listOf(
                    ROLE_GROUP_READ,
                    ROLE_USER_READ,
                    ROLE_USER_CREATE,
                    ROLE_USER_UPDATE,
                ),
            )
        )
    )

    @Test
    fun `Validate full name`() {
        assertEquals("Security User", user.fullName)
    }

    @Test
    fun `Validate short name`() {
        assertEquals("Security U.", user.shortName)
    }

    @Test
    fun `Validate alternative name`() {
        assertEquals("S. User", user.alternativeName)
    }

    @Test
    fun `Validate user is master`() {
        assertTrue(masterUser.isMaster)
        assertTrue(masterGroup.isMaster)
    }

    @Test
    fun `Validate user is not master`() {
        assertFalse(commonUser.isMaster)
        assertFalse(commonGroup.isMaster)
    }

    @Test
    fun `User current roles`() {
        val roles = rolesUser.currentRoles()
        assertEquals(7, roles.size)
        assertTrue(roles.contains(ROLE_GROUP_READ))
        assertTrue(roles.contains(ROLE_GROUP_CREATE))
        assertTrue(roles.contains(ROLE_GROUP_UPDATE))
        assertTrue(roles.contains(ROLE_GROUP_DELETE))
        assertTrue(roles.contains(ROLE_USER_READ))
        assertTrue(roles.contains(ROLE_USER_CREATE))
        assertTrue(roles.contains(ROLE_USER_UPDATE))
    }

}
