package com.thomas.core.i18n

object CoreMessageI18N : BundleResolver("strings/core-strings") {

    //region CONTEXT MESSAGE

    fun coreContextSessionUserNotLogged(): String = getFormattedMessage("core.context.session.user.not-logged")

    fun coreContextSessionUserNotAllowed(): String = getFormattedMessage("core.context.session.user.not-allowed")

    //endregion CONTEXT MESSAGE

    //region EXCEPTION

    fun coreExceptionResponseMessageNoMessage(): String = getFormattedMessage("core.exception.response.message.no-message")

    fun coreExceptionEntityValidationValidationError(): String = getFormattedMessage("core.exception.entity.validation.validation-error")

    //endregion EXCEPTION

    //region GENDER

    fun coreModelGeneralGenderCisMale(): String = getFormattedMessage("core.model.general.gender.cis-male")

    fun coreModelGeneralGenderCisFemale(): String = getFormattedMessage("core.model.general.gender.cis-female")

    fun coreModelGeneralGenderNoGender(): String = getFormattedMessage("core.model.general.gender.no-gender")

    fun coreModelGeneralGenderAndrogynousGender(): String = getFormattedMessage("core.model.general.gender.androgynous-gender")

    fun coreModelGeneralGenderBiGender(): String = getFormattedMessage("core.model.general.gender.bi-gender")

    fun coreModelGeneralGenderNonBinary(): String = getFormattedMessage("core.model.general.gender.non-binary")

    fun coreModelGeneralGenderNeutralGender(): String = getFormattedMessage("core.model.general.gender.neutral-gender")

    fun coreModelGeneralGenderTransMale(): String = getFormattedMessage("core.model.general.gender.trans-male")

    fun coreModelGeneralGenderTransFemale(): String = getFormattedMessage("core.model.general.gender.trans-female")

    fun coreModelGeneralGenderMaleTransgender(): String = getFormattedMessage("core.model.general.gender.male-transgender")

    fun coreModelGeneralGenderFemaleTransgender(): String = getFormattedMessage("core.model.general.gender.female-transgender")

    fun coreModelGeneralGenderMaleTranssexual(): String = getFormattedMessage("core.model.general.gender.male-transsexual")

    fun coreModelGeneralGenderFemaleTranssexual(): String = getFormattedMessage("core.model.general.gender.female-transsexual")

    fun coreModelGeneralGenderOtherGender(): String = getFormattedMessage("core.model.general.gender.other-gender")

    //endregion GENDER

    //region SECURITY USER PROFILE

    fun coreModelSecurityProfileCommonUser(): String = getFormattedMessage("core.model.security.profile.common-user")

    fun coreModelSecurityProfileAdministratorUser(): String = getFormattedMessage("core.model.security.profile.administrator-user")

    //endregion SECURITY USER PROFILE

}
