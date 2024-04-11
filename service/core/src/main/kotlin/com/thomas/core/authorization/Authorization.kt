package com.thomas.core.authorization

import com.thomas.core.HttpApplicationException.Companion.forbidden
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.i18n.CoreMessageI18N.coreContextSessionUserNotAllowed
import com.thomas.core.security.SecurityRole

fun <T> authorized(
    roles: Array<SecurityRole> = arrayOf(),
    block: () -> T
): T {

    currentUser.apply {
        if (roles.isNotEmpty() && !this.currentRoles().any { it in roles }) {
            throw forbidden(coreContextSessionUserNotAllowed())
        }
    }

    return block()
}