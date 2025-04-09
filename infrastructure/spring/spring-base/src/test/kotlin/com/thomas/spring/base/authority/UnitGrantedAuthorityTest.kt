package com.thomas.spring.base.authority

import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.model.security.SecurityUnitRole.COA_READ
import com.thomas.spring.base.exception.AuthorityException
import com.thomas.spring.base.i18n.SpringMessageI18N.authorityRoleUnitRoleNotAuthorized
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UnitGrantedAuthorityTest {

    @Test
    fun `getAuthority should return role name when unitId matches currentUnit`() {
        val unitId = randomUUID()
        currentUnit = unitId
        val authority = UnitGrantedAuthority(unitId, COA_READ)

        val result = authority.authority

        assertEquals(COA_READ.name, result)
    }

    @Test
    fun `getAuthority should throw AuthorityException when unitId does not match currentUnit`() {
        currentUnit = randomUUID()
        val authority = UnitGrantedAuthority(randomUUID(), COA_READ)

        val exception = assertThrows<AuthorityException> { authority.authority }
        assertEquals(authorityRoleUnitRoleNotAuthorized(COA_READ.name), exception.message)
    }

    @Test
    fun `getAuthority should throw AuthorityException when currentUnit is null`() {
        currentUnit = null
        val authority = UnitGrantedAuthority(randomUUID(), COA_READ)

        val exception = assertThrows<AuthorityException> { authority.authority }
        assertEquals(authorityRoleUnitRoleNotAuthorized(COA_READ.name), exception.message)
    }

}