package com.thomas.core.model.general

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.general.UserProfile.UserProfileStringsI18N.coreUserProfileString

enum class UserProfile {
    COMMON,
    ADMINISTRATOR;

    val label: String
        get() = coreUserProfileString(this.name.lowercase())

    private object UserProfileStringsI18N : BundleResolver("strings/core-user-profiles") {

        fun coreUserProfileString(name: String): String = getFormattedMessage("model.user-profile.$name.label")

    }

}
