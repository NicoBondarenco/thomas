package com.thomas.management.data.entity

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.management.data.entity.GroupBaseEntity.Companion.MAX_NAME_SIZE
import com.thomas.management.data.entity.GroupBaseEntity.Companion.MIN_NAME_SIZE
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationInvalidEntityErrorMessage
import com.thomas.management.data.securityUser
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
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
abstract class GroupEntityBaseTest<T : GroupBaseEntity> {

    companion object {

        @JvmStatic
        fun nameInvalidChars() = "!@#$%¨&*()+='\"\\|<>,:?;/^~`´{}[]§¬¢£³²¹".map {
            Arguments.of(it)
        }

        @JvmStatic
        fun validNames() = listOf(
            Arguments.of("TheAvengers"),
            Arguments.of("The Avengers 2012"),
            Arguments.of("Avengers"),
            Arguments.of("Âvéñgèrs"),
            Arguments.of("4v3ng3r8"),
            Arguments.of("The.Avengers"),
            Arguments.of("The_Avengers"),
            Arguments.of("The-Avengers"),
        )

    }

    @Suppress("LongParameterList")
    abstract fun entity(
        id: UUID = randomUUID(),
        groupName: String = "The Avengers",
        groupDescription: String? = "A group of heroes",
        isActive: Boolean = true,
        creatorId: UUID = currentUser.userId,
        createdAt: OffsetDateTime = now(UTC),
        updatedAt: OffsetDateTime = now(UTC),
    ): T

    @BeforeEach
    internal fun setUp() {
        currentUser = securityUser
    }

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @ParameterizedTest
    @MethodSource("validNames")
    fun `WHEN entity data is valid THEN should not throws exception`(name: String) {
        assertDoesNotThrow { entity(groupName = name) }
    }

    @Test
    fun `WHEN first name has less than 5 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity(groupName = "AAAA") }
        assertEquals(managementGroupValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = GroupEntity::groupName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @Test
    fun `WHEN first name has more than 250 characters THEN should throws EntityValidationException`() {
        val exception = assertThrows<EntityValidationException> { entity(groupName = "A".repeat(251)) }
        assertEquals(managementGroupValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = GroupEntity::groupName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE), details[field]!!.first())
    }

    @ParameterizedTest
    @MethodSource("nameInvalidChars")
    fun `WHEN first name has invalid character THEN should throws EntityValidationException`(char: Char) {
        val exception = assertThrows<EntityValidationException> { entity(groupName = "Avengers$char") }
        assertEquals(managementGroupValidationInvalidEntityErrorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = GroupEntity::groupName.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(managementGroupValidationGroupNameInvalidValue(), details[field]!!.first())
    }

}
