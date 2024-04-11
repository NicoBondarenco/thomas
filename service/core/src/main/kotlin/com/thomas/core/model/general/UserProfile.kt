package com.thomas.core.model.general

import com.thomas.core.i18n.CoreMessageI18N.coreModelSecurityProfileAdministratorUser
import com.thomas.core.i18n.CoreMessageI18N.coreModelSecurityProfileCommonUser


enum class UserProfile(val label: () -> String) {
    COMMON({ coreModelSecurityProfileCommonUser() }),
    ADMINISTRATOR({ coreModelSecurityProfileAdministratorUser() }),
}