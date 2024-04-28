package com.thomas.core.model.general

import com.thomas.core.context.SessionContextHolder
import com.thomas.core.model.general.UserProfile.ADMINISTRATOR
import com.thomas.core.model.general.UserProfile.COMMON
import java.util.Locale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class UserProfileTest {

    @Test
    fun `User Profile PT_BR`() {
        SessionContextHolder.context.currentLocale = Locale.forLanguageTag("pt-BR")
        assertEquals("Comum", COMMON.label)
        assertEquals("Administrador", ADMINISTRATOR.label)
    }

    @Test
    fun `User Profile EN_US`() {
        SessionContextHolder.context.currentLocale = Locale.ENGLISH
        assertEquals("Common", COMMON.label)
        assertEquals("Administrator", ADMINISTRATOR.label)
    }

    @Test
    fun `User Profile CH`() {
        SessionContextHolder.context.currentLocale = Locale.CHINA
        assertEquals("Common", COMMON.label)
        assertEquals("Administrator", ADMINISTRATOR.label)
    }

}
