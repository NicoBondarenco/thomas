package com.thomas.management.data.entity.info

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.management.data.entity.EntityValidationTest
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementContactValidationMainEmailInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementContactValidationMainPhoneInvalidValue
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class ContactInfoTest : EntityValidationTest() {

    private val entity: TestContactInfo
        get() = TestContactInfo()

    private val invalidEmailExecutions = listOf(
        "tony.stark@email..com",
        "tony.stark@emailcom",
        "tony.starkemail.com",
        "tony.stark_email.com",
        "tony@stark@email.com",
        "tony.stark@email",
        "tony.stark@.com",
        "@email.com",
    ).map {
        InvalidDataInput(
            description = "Invalid e-mail",
            value = it,
            execution = { entity.copy(mainEmail = it) },
            property = ContactInfo::mainEmail,
            message = managementContactValidationMainEmailInvalidValue(),
        )
    }

    private val invalidPhonesExecution = listOf(
        "+5516988776655",
        "16 988776655",
        "1698877-6655",
        "(16) 98877-6655",
        "(16)98877-6655",
        "16.988776655",
        "9.88776655",
        "",
        " ",
    ).map {
        InvalidDataInput(
            description = "Invalid phone",
            value = it,
            execution = { entity.copy(mainPhone = it) },
            property = ContactInfo::mainPhone,
            message = managementContactValidationMainPhoneInvalidValue(),
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> = invalidEmailExecutions + invalidPhonesExecution

    @Test
    fun `Valid Contact Info`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }

    private data class TestContactInfo(
        override val id: UUID = randomUUID(),
        override val mainEmail: String = randomEmail(),
        override val mainPhone: String = randomPhone(),
    ) : BaseEntity<TestContactInfo>(), ContactInfo {

        init {
            validate()
        }

        override fun validations(): List<EntityValidation<TestContactInfo>> = contactInfoValidations()

    }

}
