package com.thomas.management.domain.adapter

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.management.domain.mock.organizationProducerMock
import com.thomas.management.domain.mock.userProducerMock
import com.thomas.management.domain.util.securityOrganization
import com.thomas.management.domain.util.securityUser
import io.mockk.clearMocks
import java.util.UUID
import java.util.stream.Stream
import kotlin.reflect.KProperty1
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows

abstract class DomainValidationTest {

    protected abstract fun executions(): List<InvalidDataInput<*, *>>

    protected abstract fun errorMessage(): String

    @BeforeEach
    protected open fun beforeEach() {
        clearMocks(
            organizationProducerMock,
            userProducerMock,
            answers = false,
            recordedCalls = true,
            childMocks = false,
            verificationMarks = true,
            exclusionRules = false,
        )
    }

    @TestFactory
    fun invalidValuesTests(): Stream<DynamicTest> = DynamicTest.stream(
        executions().stream(),
        { "${it.description} for ${it.property.name} - ${it.value}" },
        {
            validateException(
                assertThrows<EntityValidationException> {
                    runTest { it.execution() }
                },
                it.property,
                it.message,
            )
            runTest { it.extraValidations() }
        }
    )

    protected data class InvalidDataInput<T, E>(
        val description: String,
        val value: String,
        val execution: suspend () -> E,
        val property: KProperty1<T, *>,
        val message: String,
        val extraValidations: suspend () -> Unit = {}
    )

    protected fun validateException(
        exception: EntityValidationException,
        property: KProperty1<*, *>,
        message: String,
    ) {
        assertEquals(errorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = property.name.toSnakeCase()
        assertEquals(1, details.size, details.errorListMessage())
        assertTrue(details.containsKey(field))
        assertEquals(1, details[field]!!.size)
        assertEquals(message, details[field]!!.first())
    }

    protected fun userWithOrganizationRole(
        role: SecurityOrganizationRole,
        organizationId: UUID = UUID.randomUUID(),
    ) {
        currentUser = securityUser.copy(
            userOrganization = securityOrganization.copy(
                organizationId = organizationId,
                organizationRoles = setOf(role),
            ),
        )
    }

    private fun Map<String, List<String>>.errorListMessage() = this.entries.joinToString("\n") {
        "${it.key} - ${it.value.joinToString("; ")}"
    }

}
