package com.thomas.core.model.general

import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderAndrogynousGender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderBiGender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderCisFemale
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderCisMale
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderFemaleTransgender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderFemaleTranssexual
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderMaleTransgender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderMaleTranssexual
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderNeutralGender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderNoGender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderNonBinary
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderOtherGender
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderTransFemale
import com.thomas.core.i18n.CoreMessageI18N.coreModelGeneralGenderTransMale

enum class Gender(val label: () -> String) {
    CIS_MALE({ coreModelGeneralGenderCisMale() }),
    CIS_FEMALE({ coreModelGeneralGenderCisFemale() }),
    NO_GENDER({ coreModelGeneralGenderNoGender() }),
    ANDROGYNOUS_GENDER({ coreModelGeneralGenderAndrogynousGender() }),
    BI_GENDER({ coreModelGeneralGenderBiGender() }),
    NON_BINARY({ coreModelGeneralGenderNonBinary() }),
    NEUTRAL_GENDER({ coreModelGeneralGenderNeutralGender() }),
    TRANS_MALE({ coreModelGeneralGenderTransMale() }),
    TRANS_FEMALE({ coreModelGeneralGenderTransFemale() }),
    MALE_TRANSGENDER({ coreModelGeneralGenderMaleTransgender() }),
    FEMALE_TRANSGENDER({ coreModelGeneralGenderFemaleTransgender() }),
    MALE_TRANSSEXUAL({ coreModelGeneralGenderMaleTranssexual() }),
    FEMALE_TRANSSEXUAL({ coreModelGeneralGenderFemaleTranssexual() }),
    OTHER_GENDER({ coreModelGeneralGenderOtherGender() });
}