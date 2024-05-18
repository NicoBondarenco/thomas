package com.thomas.management.data.entity

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.general.UserProfile.COMMON
import com.thomas.management.data.entity.UserEntity.Companion.MAX_NAME_SIZE
import com.thomas.management.data.entity.UserEntity.Companion.MIN_NAME_SIZE
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationMainEmailInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationPhoneNumberInvalidCharacter
import com.thomas.management.data.securityUser
import java.time.LocalDate
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("UnusedDataClassCopyResult")
class UserEntityTest {

    companion object {

        @JvmStatic
        fun nameInvalidChars() = "9876543210!@#$%¨&*()_+='\"\\|<>,.:?;/^~`´{}[]§¬¢£³²¹".map {
            Arguments.of(it)
        }

        @JvmStatic
        fun invalidEmails() = listOf(
            Arguments.of("tony.stark@email..com"),
            Arguments.of("tony.stark@emailcom"),
            Arguments.of("tony.starkemail.com"),
            Arguments.of("tony.stark_email.com"),
            Arguments.of("tony@stark@email.com"),
            Arguments.of("tony.stark@email"),
            Arguments.of("tony.stark@.com"),
            Arguments.of("@email.com"),
        )

        @JvmStatic
        fun invalidDocuments() = listOf(
            Arguments.of("946 26 150-55"),
            Arguments.of("530224560420"),
            Arguments.of("891551820-99"),
            Arguments.of("346.017.430-24"),
            Arguments.of("789.940-48"),
            Arguments.of("549-009-210-69"),
            Arguments.of("111.111.111-11"),
            Arguments.of(""),
            Arguments.of(" "),
        )

        @JvmStatic
        fun invalidPhones() = listOf(
            Arguments.of("+5516988776655"),
            Arguments.of("16 988776655"),
            Arguments.of("1698877-6655"),
            Arguments.of("(16) 98877-6655"),
            Arguments.of("(16)98877-6655"),
            Arguments.of("16.988776655"),
            Arguments.of("9.88776655"),
            Arguments.of(""),
            Arguments.of(" "),
        )

    }

    private fun entity() = UserEntity(
        id = randomUUID(),
        firstName = "Tony",
        lastName = "Stark",
        mainEmail = "tony.stark@email.com",
        documentNumber = "58370027075",
        phoneNumber = "5516988776655",
        profileType = COMMON,
        profilePhoto = null,
        birthDate = LocalDate.of(1990, 4, 28),
        userGender = CIS_MALE,
        addressId = null,
        isActive = true,
        creatorId = currentUser.userId,
        createdAt = now(UTC),
        updatedAt = now(UTC),
        userRoles = mutableListOf(),
    )

    @BeforeEach
    internal fun setUp() {
        currentUser = securityUser
    }

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @Test
    fun `WHEN entity data is valid THEN should not throws exception`() {
        assertDoesNotThrow { entity() }
    }

    @Test
    fun `WHEN first name has less than 2 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity().copy(firstName = "A") }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::firstName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @Test
    fun `WHEN first name has more than 250 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity().copy(firstName = "A".repeat(251)) }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::firstName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("nameInvalidChars")
    fun `WHEN first name has invalid character THEN should throws EntityValidationException`(char: Char) {
        val exception = assertThrows<EntityValidationException> { entity().copy(firstName = "Tony$char") }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::firstName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationFirstNameInvalidValue(), details[field]!!.first())
    }

    @Test
    fun `WHEN last name has less than 2 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity().copy(lastName = "A") }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::lastName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @Test
    fun `WHEN last name has more than 250 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity().copy(lastName = "A".repeat(251)) }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::lastName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("nameInvalidChars")
    fun `WHEN last name has invalid character THEN should throws EntityValidationException`(char: Char) {
        val exception = assertThrows<EntityValidationException> { entity().copy(lastName = "Stark$char") }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::lastName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationLastNameInvalidValue(), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    fun `WHEN email has invalid format THEN should throws EntityValidationException`(email: String) {
        val exception = assertThrows<EntityValidationException> { entity().copy(mainEmail = email) }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::mainEmail.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationMainEmailInvalidValue(), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("invalidDocuments")
    fun `WHEN document number is invalid THEN should throws EntityValidationException`(document: String) {
        val exception = assertThrows<EntityValidationException> { entity().copy(documentNumber = document) }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::documentNumber.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationDocumentNumberInvalidValue(), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("invalidPhones")
    fun `WHEN phone number is invalid THEN should throws EntityValidationException`(phone: String) {
        val exception = assertThrows<EntityValidationException> { entity().copy(phoneNumber = phone) }
        assertEquals(managementUserValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = UserEntity::phoneNumber.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementUserValidationPhoneNumberInvalidCharacter(), details[field]!!.first())
    }

}
