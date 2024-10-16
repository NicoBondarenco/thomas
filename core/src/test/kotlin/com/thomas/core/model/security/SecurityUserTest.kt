package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.generator.GroupGenerator.generateSecurityGroup
import com.thomas.core.generator.OrganizationUnitGenerator.generateSecurityOrganization
import com.thomas.core.generator.OrganizationUnitGenerator.generateSecurityUnit
import com.thomas.core.generator.UserGenerator.generateSecurityUser
import com.thomas.core.model.security.SecurityOrganizationRole.GROUP_CREATE
import com.thomas.core.model.security.SecurityOrganizationRole.GROUP_READ
import com.thomas.core.model.security.SecurityOrganizationRole.GROUP_UPDATE
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_READ
import com.thomas.core.model.security.SecurityOrganizationRole.USER_CREATE
import com.thomas.core.model.security.SecurityOrganizationRole.USER_READ
import com.thomas.core.model.security.SecurityOrganizationRole.USER_UPDATE
import com.thomas.core.model.security.SecurityUnitRole.COA_CREATE
import com.thomas.core.model.security.SecurityUnitRole.COA_READ
import com.thomas.core.model.security.SecurityUnitRole.COA_UPDATE
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SecurityUserTest {

    @BeforeEach
    internal fun setUp() {
        clearContext()
    }

    @Test
    fun `Security User full name`() {
        val user = generateSecurityUser()
        assertEquals("${user.firstName} ${user.lastName}", user.fullName)
    }

    @Test
    fun `Security User short name`() {
        val user = generateSecurityUser()
        assertEquals("${user.firstName} ${user.lastName[0]}.", user.shortName)
    }

    @Test
    fun `Security User alternative name`() {
        val user = generateSecurityUser()
        assertEquals("${user.firstName[0]}. ${user.lastName}", user.alternativeName)
    }

    @Test
    fun `Security User is master`() {
        val user = generateSecurityUser().copy(
            userOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf(ORGANIZATION_ALL)
            ),
            userGroups = setOf()
        )

        assertTrue(user.isMaster)
    }

    @Test
    fun `Security User is master by group`() {
        val user = generateSecurityUser().copy(
            userOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf()
            ),
            userGroups = setOf(
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(ORGANIZATION_ALL)
                    )
                )
            )
        )

        assertTrue(user.isMaster)
    }

    @Test
    fun `Security User Organization roles`() {
        val user = generateSecurityUser().copy(
            userOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf(USER_READ, USER_CREATE, USER_UPDATE)
            ),
            userGroups = setOf(
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(GROUP_READ, GROUP_CREATE, GROUP_UPDATE)
                    )
                ),
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(USER_READ, GROUP_READ, UNIT_READ)
                    )
                ),
            )
        )

        assertEquals(7, user.organizationRoles.size)
        assertTrue(
            user.organizationRoles.containsAll(
                setOf(
                    USER_READ, USER_CREATE, USER_UPDATE,
                    GROUP_READ, GROUP_CREATE, GROUP_UPDATE,
                    UNIT_READ
                )
            )
        )
    }

    @Test
    fun `Security User Unit roles without current Unit`() {
        val user = generateSecurityUser()
        assertTrue(user.unitRoles.isEmpty())
    }

    @Test
    fun `Security User Unit roles`() {
        val unitId = UUID.randomUUID()
        currentUnit = unitId
        val user = generateSecurityUser().copy(
            userUnits = setOf(
                generateSecurityUnit().copy(
                    unitRoles = setOf()
                ),
                generateSecurityUnit().copy(
                    unitId = unitId,
                    unitRoles = setOf(COA_READ)
                ),
            ),
            userGroups = setOf(
                generateSecurityGroup().copy(
                    groupUnits = setOf()
                ),
                generateSecurityGroup().copy(
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_READ)
                        ),
                    )
                ),
                generateSecurityGroup().copy(
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitRoles = setOf(COA_READ)
                        ),
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_CREATE)
                        ),
                    )
                ),
                generateSecurityGroup().copy(
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitRoles = setOf(COA_UPDATE)
                        ),
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_CREATE)
                        ),
                    )
                ),
            )
        )

        assertEquals(2, user.unitRoles.size)
        assertTrue(user.unitRoles.containsAll(setOf(COA_READ, COA_CREATE)))
    }

    @Test
    fun `Security User current roles`() {
        val unitId = UUID.randomUUID()
        currentUnit = unitId
        val user = generateSecurityUser().copy(
            userOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf(USER_READ, USER_CREATE, USER_UPDATE)
            ),
            userUnits = setOf(
                generateSecurityUnit().copy(
                    unitRoles = setOf()
                ),
                generateSecurityUnit().copy(
                    unitId = unitId,
                    unitRoles = setOf(COA_READ)
                ),
            ),
            userGroups = setOf(
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(GROUP_READ, GROUP_CREATE, GROUP_UPDATE)
                    ),
                    groupUnits = setOf()
                ),
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf()
                    ),
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_READ)
                        ),
                    )
                ),
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(USER_READ, GROUP_READ, UNIT_READ)
                    ),
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitRoles = setOf(COA_READ)
                        ),
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_CREATE)
                        ),
                    )
                ),
                generateSecurityGroup().copy(
                    groupOrganization = generateSecurityOrganization().copy(
                        organizationRoles = setOf(USER_READ, GROUP_READ, UNIT_READ)
                    ),
                    groupUnits = setOf(
                        generateSecurityUnit().copy(
                            unitRoles = setOf(COA_UPDATE)
                        ),
                        generateSecurityUnit().copy(
                            unitId = unitId,
                            unitRoles = setOf(COA_CREATE)
                        ),
                    )
                ),
            )
        )

        assertEquals(9, user.currentRoles.size)
        assertTrue(
            user.currentRoles.containsAll(
                setOf(
                    USER_READ, USER_CREATE, USER_UPDATE,
                    GROUP_READ, GROUP_CREATE, GROUP_UPDATE,
                    UNIT_READ,
                    COA_READ, COA_CREATE
                )
            )
        )
    }

}