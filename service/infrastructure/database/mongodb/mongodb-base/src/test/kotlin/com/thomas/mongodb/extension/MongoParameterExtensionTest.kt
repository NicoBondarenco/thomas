package com.thomas.mongodb.extension

import com.thomas.mongodb.data.ParentTestEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MongoParameterExtensionTest {

    @Test
    fun `MongoDB id field name mus be _id`() {
        assertEquals(OBJECT_ID_PARAMETER_NAME, "_id")
    }

    @Test
    fun `Field name to mongo parameter must be quoted`() {
        assertEquals("\$parentName", ParentTestEntity::parentName.mongoParameter)
    }

}
