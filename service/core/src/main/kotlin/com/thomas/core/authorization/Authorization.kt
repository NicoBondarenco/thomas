package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.i18n.CoreMessageI18N.contextCurrentSessionCurrentUserNotAllowed
import com.thomas.core.model.security.SecurityRole

fun <T> authorized(
    roles: Array<SecurityRole> = arrayOf(),
    block: () -> T
): T = if (roles.isAuthorized()) {
    block()
} else {
    throw UnauthorizedUserException(contextCurrentSessionCurrentUserNotAllowed())
}

private fun Array<SecurityRole>.isAuthorized(): Boolean =
    this.isEmpty() || currentUser.currentRoles().intersect(this.toSet()).isNotEmpty()
