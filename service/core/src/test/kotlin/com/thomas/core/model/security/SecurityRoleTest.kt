package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.model.security.SecurityRole
import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRoleGroup
import com.thomas.core.model.security.SecurityRoleGroup.MANAGEMENT
import com.thomas.core.model.security.SecurityRoleSubgroup
import com.thomas.core.model.security.SecurityRoleSubgroup.FINANCE_DATA
import java.util.Locale
import java.util.Locale.ROOT
import java.util.Properties
import kotlin.test.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SecurityRoleTest {

    private val propertiesRoot = Properties().apply {
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-groups.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-subgroups.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles.properties"))
    }

    private val propertiesBr = Properties().apply {
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-groups_pt_BR.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-subgroups_pt_BR.properties"))
        this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles_pt_BR.properties"))
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
            assertEquals(propertiesRoot.getProperty("core.role.group.${group!!.name.lowercase()}.name"), group.groupName)
            assertEquals(propertiesRoot.getProperty("core.role.group.${group.name.lowercase()}.description"), group.groupDescription)
            assertFalse(group.subgroups().isEmpty())
        }
    }

    @Test
    fun `Security Role Subgroup test`() {
        SecurityRoleSubgroup.entries.map { it.subgroupOrder }.forEach { order ->
            val subgroup = SecurityRoleSubgroup.entries.firstOrNull { it.subgroupOrder == order }
            assertNotNull(subgroup)
            assertEquals(propertiesRoot.getProperty("core.role.subgroup.${subgroup!!.name.lowercase()}.name"), subgroup.subgroupName)
            assertEquals(propertiesRoot.getProperty("core.role.subgroup.${subgroup.name.lowercase()}.description"), subgroup.subgroupDescription)
            assertFalse(subgroup.roles().isEmpty())
        }
    }

    @Test
    fun `Security Role test`() {
        SecurityRole.entries.map { Triple(it.roleCode, it.roleOrder, it.roleDisplayable) }.forEach { order ->
            val role = SecurityRole.entries.firstOrNull { it.roleCode == order.first && it.roleOrder == order.second && it.roleDisplayable == order.third }
            assertNotNull(role)
            assertEquals(propertiesRoot.getProperty("core.roles.${role!!.name.lowercase()}.name"), role.roleName)
            assertEquals(propertiesRoot.getProperty("core.roles.${role.name.lowercase()}.description"), role.roleDescription)
        }
    }

    @Test
    fun `Security Role by Code`() {
        assertEquals(MASTER, SecurityRole.byCode(0))
    }

    @Test
    fun `Security Role by Code not found`() {
        assertNull(SecurityRole.byCode(987654321))
    }

    @Test
    fun `Names in pt-BR`() {
        currentLocale = Locale.forLanguageTag("pt-BR")

        val group = MANAGEMENT
        val subgroup = FINANCE_DATA
        val role = ROLE_GROUP_CREATE

        assertEquals(propertiesBr.getProperty("core.role.group.${group.name.lowercase()}.name"), group.groupName)
        assertEquals(propertiesBr.getProperty("core.role.group.${group.name.lowercase()}.description"), group.groupDescription)

        assertEquals(propertiesBr.getProperty("core.role.subgroup.${subgroup.name.lowercase()}.name"), subgroup.subgroupName)
        assertEquals(propertiesBr.getProperty("core.role.subgroup.${subgroup.name.lowercase()}.description"), subgroup.subgroupDescription)

        assertEquals(propertiesBr.getProperty("core.roles.${role.name.lowercase()}.name"), role.roleName)
        assertEquals(propertiesBr.getProperty("core.roles.${role.name.lowercase()}.description"), role.roleDescription)

    }

}