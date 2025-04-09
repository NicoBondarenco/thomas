package com.thomas.spring.base.authority

import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.spring.base.exception.AuthorityException
import com.thomas.spring.base.i18n.SpringMessageI18N.authorityRoleOrganizationRoleNotAuthorized
import java.util.UUID
import org.springframework.security.core.GrantedAuthority

class OrganizationGrantedAuthority(
    private val organizationId: UUID,
    private val organizationRole: SecurityOrganizationRole
) : GrantedAuthority {

    override fun getAuthority(): String = organizationRole.takeIf {
        organizationId == currentOrganization
    }?.name ?: throw AuthorityException(authorityRoleOrganizationRoleNotAuthorized(organizationRole.name))

}
