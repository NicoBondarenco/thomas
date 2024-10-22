package com.thomas.management.data.entity

import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import java.util.stream.Stream
import kotlin.reflect.KProperty1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows

abstract class EntityValidationTest {

    protected abstract fun executions(): List<InvalidDataInput<*, *>>

    @TestFactory
    fun invalidValuesTests(): Stream<DynamicTest> = DynamicTest.stream(
        executions().stream(),
        { "${it.description} for ${it.property.name} - ${it.value}" },
        {
            validateException(
                assertThrows<EntityValidationException> {
                    it.execution()
                },
                it.property,
                it.message,
            )
        }
    )

    protected data class InvalidDataInput<T, E : T>(
        val description: String,
        val value: String,
        val execution: () -> E,
        val property: KProperty1<T, *>,
        val message: String,
    )

    protected fun validateException(
        exception: EntityValidationException,
        property: KProperty1<*, *>,
        message: String,
    ) {
        val details = (exception.detail as? Map<String, List<String>>)!!
        val field = property.name.toSnakeCase()
        assertEquals(1, details.size, details.errorListMessage())
        assertTrue(details.containsKey(field))
        assertEquals(1, details[field]!!.size)
        assertEquals(message, details[field]!!.first())
    }

    private fun Map<String, List<String>>.errorListMessage() = this.entries.joinToString("\n") {
        "${it.key} - ${it.value.joinToString("; ")}"
    }

}
