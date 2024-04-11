package com.thomas.core.context

import com.thomas.core.security.SecurityUser
import java.util.Locale

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

    fun getSessionProperty(property: String): String? = context.getProperty(property)

    fun setSessionProperty(property: String, value: String?) = context.setProperty(property, value)

    fun clearContext() = context.clearContext()
}