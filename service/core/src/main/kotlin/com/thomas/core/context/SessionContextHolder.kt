package com.thomas.core.context

import com.thomas.core.model.security.SecurityUser
import java.util.Locale
import java.util.UUID

object SessionContextHolder {

    private val contextHolder = ThreadLocal<SessionContext?>()

    val context: SessionContext
        get() = contextHolder.get() ?: SessionContext().also { contextHolder.set(it) }

    var currentUser: SecurityUser
        get() = context.currentUser
        set(value) {
            context.currentUser = value
        }

    var currentToken: String?
        get() = context.currentToken
        set(value) {
            context.currentToken = value
        }

    var currentLocale: Locale
        get() = context.currentLocale
        set(value) {
            context.currentLocale = value
        }

    val currentUserId: UUID?
        get() = context.currentUserId

    fun getSessionProperty(property: String): String? = context.getProperty(property)

    fun setSessionProperty(property: String, value: String?) = context.setProperty(property, value)

    fun clearContext() = context.clear()
}
