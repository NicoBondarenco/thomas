package com.thomas.core.authorization

import com.thomas.core.HttpApplicationException
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.i18n.CoreMessageI18N.coreContextSessionUserNotAllowed
import com.thomas.core.model.http.HTTPStatus.FORBIDDEN
import com.thomas.core.model.security.SecurityRole

fun <T> authorized(
    roles: Array<SecurityRole> = arrayOf(),
    block: () -> T
): T = if (roles.isAuthorized()) {
    block()
} else {
    throw HttpApplicationException(FORBIDDEN, coreContextSessionUserNotAllowed())
}

private fun Array<SecurityRole>.isAuthorized(): Boolean =
    this.isEmpty() || currentUser.currentRoles().intersect(this.toSet()).isNotEmpty()
