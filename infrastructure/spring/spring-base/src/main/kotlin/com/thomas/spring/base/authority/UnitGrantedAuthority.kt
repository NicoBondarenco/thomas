package com.thomas.spring.base.authority

import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.spring.base.exception.AuthorityException
import com.thomas.spring.base.i18n.SpringMessageI18N.authorityRoleUnitRoleNotAuthorized
import java.util.UUID
import org.springframework.security.core.GrantedAuthority

class UnitGrantedAuthority(
    private val unitId: UUID,
    private val unitRole: SecurityUnitRole
) : GrantedAuthority {

    override fun getAuthority(): String = unitRole.takeIf { unitId == currentUnit }?.name
        ?: throw AuthorityException(authorityRoleUnitRoleNotAuthorized(unitRole.name))

}
