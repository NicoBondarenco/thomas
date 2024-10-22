package com.thomas.management.data.entity.info

import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementContactValidationMainEmailInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementContactValidationMainPhoneInvalidValue

interface ContactInfo {

    companion object {
        private val EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
        private val PHONE_NUMBER_REGEX = "[0-9]{10,11}".toRegex()
    }

    val mainEmail: String
    val mainPhone: String

    fun <T> contactInfoValidations(): List<EntityValidation<T>> where T : ContactInfo, T : BaseEntity<T> = listOf(
        EntityValidation(
            ContactInfo::mainEmail.name.toSnakeCase(),
            { managementContactValidationMainEmailInvalidValue() },
            { EMAIL_REGEX.matches(it.mainEmail) }
        ),
        EntityValidation(
            ContactInfo::mainPhone.name.toSnakeCase(),
            { managementContactValidationMainPhoneInvalidValue() },
            { PHONE_NUMBER_REGEX.matches(it.mainPhone) }
        ),
    )

}
