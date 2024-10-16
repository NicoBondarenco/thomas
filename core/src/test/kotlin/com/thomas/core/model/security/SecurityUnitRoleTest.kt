package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.model.security.SecurityUnitRole.COA_READ
import java.util.Locale
import java.util.Locale.ROOT
import java.util.Properties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class SecurityUnitRoleTest {

    companion object {

        @JvmStatic
        fun strings() = listOf(
            Arguments.of(ROOT, {
                Properties().apply {
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-groups.properties"))
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-subgroups.properties"))
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles.properties"))
                }
            }),
            Arguments.of(Locale.forLanguageTag("en-US"), {
                Properties().apply {
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-groups_en_US.properties"))
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles-subgroups_en_US.properties"))
                    this.load(ClassLoader.getSystemClassLoader().getResourceAsStream("strings/core-roles_en_US.properties"))
                }
            }),
        )

    }

    @BeforeEach
    internal fun setUp() {
        currentLocale = ROOT
    }

    @ParameterizedTest
    @MethodSource("strings")
    fun `Security Role Group test`(
        locale: Locale,
        props: () -> Properties,
    ) {
        currentLocale = locale
        val properties = props()
        SecurityUnitRoleGroup.entries.map { it.groupOrder }.forEach { order ->
            val group = SecurityUnitRoleGroup.entries.firstOrNull { it.groupOrder == order }
            assertNotNull(group)
            assertEquals(properties.getProperty("security.role-group.unit.${group!!.name.lowercase()}.name"), group.groupName)
            assertEquals(properties.getProperty("security.role-group.unit.${group.name.lowercase()}.description"), group.groupDescription)
            assertTrue(SecurityUnitRoleSubgroup.entries.filter { it.subgroupGroup == group }.containsAll(group.subgroups()))
        }
    }


    @ParameterizedTest
    @MethodSource("strings")
    fun `Security Role Subgroup test`(
        locale: Locale,
        props: () -> Properties,
    ) {
        currentLocale = locale
        val properties = props()
        SecurityUnitRoleSubgroup.entries.map { it.subgroupOrder }.forEach { order ->
            val subgroup = SecurityUnitRoleSubgroup.entries.firstOrNull { it.subgroupOrder == order }
            assertNotNull(subgroup)
            assertEquals(properties.getProperty("security.role-subgroup.unit.${subgroup!!.name.lowercase()}.name"), subgroup.subgroupName)
            assertEquals(properties.getProperty("security.role-subgroup.unit.${subgroup.name.lowercase()}.description"), subgroup.subgroupDescription)
            assertTrue(SecurityUnitRole.entries.filter { it.roleSubgroup == subgroup }.containsAll(subgroup.roles()))
        }
    }

    @ParameterizedTest
    @MethodSource("strings")
    fun `Security Role test`(
        locale: Locale,
        props: () -> Properties,
    ) {
        currentLocale = locale
        val properties = props()
        SecurityUnitRole.entries.map { it.roleCode to it.roleOrder }.forEach { order ->
            val role = SecurityUnitRole.entries.firstOrNull {
                it.roleCode == order.first && it.roleOrder == order.second
            }
            assertNotNull(role)
            assertEquals(properties.getProperty("security.role.unit.${role!!.name.lowercase()}.name"), role.roleName)
            assertEquals(properties.getProperty("security.role.unit.${role.name.lowercase()}.description"), role.roleDescription)
        }
    }

    @Test
    fun `Security Role by Code`() {
        assertEquals(COA_READ, SecurityUnitRole.byCode(0))
    }

    @Test
    fun `Security Role by Code not found`() {
        assertNull(SecurityUnitRole.byCode(987654321))
    }

}
