package com.thomas.exposed.properties

import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_NONE
import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_READ_COMMITTED
import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_READ_UNCOMMITTED
import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_REPEATABLE_READ
import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_SERIALIZABLE
import java.sql.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExposedIsolationLevelTest {

    @Test
    fun `Enum isolation code must be the same as java sql Connection`() {
        assertEquals(TRANSACTION_NONE.isolationCode, Connection.TRANSACTION_NONE)
        assertEquals(TRANSACTION_READ_UNCOMMITTED.isolationCode, Connection.TRANSACTION_READ_UNCOMMITTED)
        assertEquals(TRANSACTION_READ_COMMITTED.isolationCode, Connection.TRANSACTION_READ_COMMITTED)
        assertEquals(TRANSACTION_REPEATABLE_READ.isolationCode, Connection.TRANSACTION_REPEATABLE_READ)
        assertEquals(TRANSACTION_SERIALIZABLE.isolationCode, Connection.TRANSACTION_SERIALIZABLE)
    }

}
