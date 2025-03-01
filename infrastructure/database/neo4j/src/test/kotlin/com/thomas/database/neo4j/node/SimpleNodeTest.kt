package com.thomas.database.neo4j.node

import com.thomas.core.util.NumberUtils.randomBigDecimal
import com.thomas.core.util.NumberUtils.randomBigInteger
import com.thomas.core.util.NumberUtils.randomDouble
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import com.thomas.core.util.StringUtils.randomString
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SimpleNodeTest {

    @Test
    fun `Node name test`() {
        assertEquals(
            "BooleanProps",
            BooleanPropsNode(
                id = randomUUID(),
                propBoolean = true
            ).nodeName()
        )
        assertEquals(
            "StringProps",
            StringPropsNode(
                id = randomUUID(),
                propName = randomString()
            ).nodeName()
        )
        assertEquals(
            "NumberProps",
            NumberPropsNode(
                id = randomUUID(),
                propInteger = randomInteger(),
                propLong = randomLong(),
                propBiginteger = randomBigInteger(),
                propDouble = randomDouble(),
                propBigdecimal = randomBigDecimal(),
            ).nodeName()
        )
    }

}