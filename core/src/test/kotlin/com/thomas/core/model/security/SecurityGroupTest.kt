package com.thomas.core.model.security

import com.thomas.core.generator.OrganizationUnitGenerator.generateSecurityOrganization
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_CREATE
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SecurityGroupTest {

    @Test
    fun `SecurityGroup has master role`() {
        SecurityGroup(
            groupId = randomUUID(),
            groupName = randomString(),
            groupOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf(ORGANIZATION_ALL)
            ),
            groupUnits = setOf(),
        ).apply {
            assertTrue(this.isMaster)
        }
    }

    @Test
    fun `SecurityGroup does not have master role`() {
        SecurityGroup(
            groupId = randomUUID(),
            groupName = randomString(),
            groupOrganization = generateSecurityOrganization().copy(
                organizationRoles = setOf(UNIT_CREATE)
            ),
            groupUnits = setOf(),
        ).apply {
            assertFalse(this.isMaster)
        }
    }

}
