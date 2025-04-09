package com.thomas.spring.base.authority

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.data.securityUser
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.spring.base.exception.AuthorityException
import com.thomas.spring.base.i18n.SpringMessageI18N.authorityRoleOrganizationRoleNotAuthorized
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationGrantedAuthorityTest {

    @Test
    fun `getAuthority should return role name when organizationId matches currentOrganization`() {
        val user = securityUser
        currentUser = user

        val organizationId = user.userOrganization.organizationId
        val organizationRole = ORGANIZATION_ALL
        val authority = OrganizationGrantedAuthority(organizationId, organizationRole)

        val result = authority.authority

        assertEquals(organizationRole.name, result)
    }

    @Test
    fun `getAuthority should throw AuthorityException when organizationId does not match currentOrganization`() {
        currentUser = securityUser

        val organizationId = randomUUID()
        val organizationRole = ORGANIZATION_ALL

        val authority = OrganizationGrantedAuthority(organizationId, organizationRole)

        val exception = assertThrows<AuthorityException> { authority.authority }
        assertEquals(authorityRoleOrganizationRoleNotAuthorized(organizationRole.name), exception.message)
    }

}