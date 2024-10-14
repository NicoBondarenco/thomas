package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityRole
import kotlinx.coroutines.coroutineScope

suspend fun <T> authorized(
    roles: Array<SecurityRole<*, *, *>> = arrayOf(),
    block: suspend () -> T
): T = coroutineScope {
    if (roles.isAuthorized()) {
        block()
    } else {
        throw UnauthorizedUserException()
    }
}

private suspend fun Array<SecurityRole<*, *, *>>.isAuthorized(): Boolean = coroutineScope {
    this@isAuthorized.isEmpty() || currentUser.currentRoles.intersect(this@isAuthorized.toSet()).isNotEmpty()
}