package com.thomas.core.security

import com.thomas.core.context.SessionContextHolder.currentLocale
import java.util.Locale.ROOT
import java.util.Properties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SecurityRoleTest {

    private val properties = Properties().apply {
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-groups.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-subgroups.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles.properties"))
    }

    @BeforeEach
    internal fun setUp() {
        currentLocale = ROOT
    }

    @Test
    fun `Security Role Group test`() {
        SecurityRoleGroup.entries.map { it.groupOrder }.forEach { order ->
            val group = SecurityRoleGroup.entries.firstOrNull { it.groupOrder == order }
            assertNotNull(group)
            assertEquals(properties.getProperty("core.role.group.${group!!.name.lowercase()}.name"), group.groupName())
            assertEquals(properties.getProperty("core.role.group.${group.name.lowercase()}.description"), group.groupDescription())
            assertFalse(group.subgroups().isEmpty())
        }
    }

    @Test
    fun `Security Role Subgroup test`() {
        SecurityRoleSubgroup.entries.map { it.subgroupOrder }.forEach { order ->
            val subgroup = SecurityRoleSubgroup.entries.firstOrNull { it.subgroupOrder == order }
            assertNotNull(subgroup)
            assertEquals(properties.getProperty("core.role.subgroup.${subgroup!!.name.lowercase()}.name"), subgroup.subgroupName())
            assertEquals(properties.getProperty("core.role.subgroup.${subgroup.name.lowercase()}.description"), subgroup.subgroupDescription())
            assertFalse(subgroup.roles().isEmpty())
        }
    }

    @Test
    fun `Security Role test`() {
        SecurityRole.entries.map { Triple(it.roleCode, it.roleOrder, it.roleDisplayable) }.forEach { order ->
            val role = SecurityRole.entries.firstOrNull { it.roleCode == order.first && it.roleOrder == order.second && it.roleDisplayable == order.third }
            assertNotNull(role)
            assertEquals(properties.getProperty("core.roles.${role!!.name.lowercase()}.name"), role.roleName())
            assertEquals(properties.getProperty("core.roles.${role.name.lowercase()}.description"), role.roleDescription())
        }
    }

    @Test
    fun `Security Role by Code`(){
        assertEquals(SecurityRole.MASTER, SecurityRole.byCode(0))
    }

}