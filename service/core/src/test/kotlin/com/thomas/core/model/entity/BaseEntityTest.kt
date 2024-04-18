package com.thomas.core.model.entity

import com.thomas.core.i18n.CoreMessageI18N.coreExceptionEntityValidationValidationError
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class BaseEntityTest {

    @Test
    fun `Given a valid entity, should not throws exception`() {
        assertDoesNotThrow {
            TestEntity(UUID.randomUUID(),"Qwerty", "Qwerty")
        }
    }

    @Test
    fun `Given a invalid entity, should throws exception`() {
        val exception = assertThrows<EntityValidationException> {
            TestEntity(UUID.randomUUID(),"", "Qwerty")
        }

        assertEquals("TestEntity Error", exception.message)
        assertEquals(1, exception.errors.size)
        assertEquals("001", exception.errors[0].code)
        assertEquals("Name is invalid", exception.errors[0].message)
    }

    @Test
    fun `Given a invalid entity with no custom message, should throws exception with default error message`() {
        val exception = assertThrows<EntityValidationException> {
            NoMessageEntity(UUID.randomUUID(), "", "Qwerty")
        }

        assertEquals(coreExceptionEntityValidationValidationError(), exception.message)
        assertEquals(1, exception.errors.size)
        assertEquals("001", exception.errors[0].code)
        assertEquals("Name is invalid", exception.errors[0].message)
    }

    @Test
    fun `Given an entity with no validation, should not throws exception`() {
        assertDoesNotThrow {
            NoValidationEntity(UUID.randomUUID(),"", "")
        }
    }

    @Test
    fun `Given a valid entity, when updated with valid values, should not throws exception`() {
        assertDoesNotThrow {
            UpdatableEntity(UUID.randomUUID(),"Qwerty", "Qwerty").update {
                name = "Another"
            }
        }
    }

    @Test
    fun `Given a valid entity, when updated with invalid values, should throws exception`() {
        val exception = assertThrows<EntityValidationException> {
            UpdatableEntity(UUID.randomUUID(),"Qwerty", "Qwerty").update {
                email = ""
            }
        }

        assertEquals("UpdatableEntity Error", exception.message)
        assertEquals(1, exception.errors.size)
        assertEquals("002", exception.errors[0].code)
        assertEquals("Email is invalid", exception.errors[0].message)
    }

    private data class TestEntity(
        override val id: UUID = UUID.randomUUID(),
        val name: String,
        val email: String,
    ) : BaseEntity<TestEntity>() {

        init {
            validate()
        }

        override fun errorMessage(): String = "TestEntity Error"

        override fun validations(): List<Validation<TestEntity>> =
            listOf(
                Validation("001", { "Name is invalid" }, { it.name.trim().isNotEmpty() }),
                Validation("002", { "Email is invalid" }, { it.email.trim().isNotEmpty() }),
            )
    }

    private data class NoMessageEntity(
        override val id: UUID = UUID.randomUUID(),
        val name: String,
        val email: String,
    ) : BaseEntity<NoMessageEntity>() {

        init {
            validate()
        }

        override fun validations(): List<Validation<NoMessageEntity>> =
            listOf(
                Validation("001", { "Name is invalid" }, { it.name.trim().isNotEmpty() }),
                Validation("002", { "Email is invalid" }, { it.email.trim().isNotEmpty() }),
            )
    }

    private data class NoValidationEntity(
        override val id: UUID = UUID.randomUUID(),
        val name: String,
        val email: String,
    ) : BaseEntity<NoValidationEntity>() {

        init {
            validate()
        }

    }

    private data class UpdatableEntity(
        override val id: UUID = UUID.randomUUID(),
        var name: String,
        var email: String,
    ) : BaseEntity<UpdatableEntity>() {

        init {
            validate()
        }

        override fun errorMessage(): String = "UpdatableEntity Error"

        override fun validations(): List<Validation<UpdatableEntity>> =
            listOf(
                Validation("001", { "Name is invalid" }, { it.name.trim().isNotEmpty() }),
                Validation("002", { "Email is invalid" }, { it.email.trim().isNotEmpty() }),
            )
    }

}