package com.thomas.database.neo4j.node

import com.thomas.database.neo4j.converter.BigDecimalConverter
import com.thomas.database.neo4j.converter.BigIntegerConverter
import com.thomas.database.neo4j.converter.UUIDConverter
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "NumberProps")
data class NumberPropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "prop_integer")
    var propInteger: Int,

    @Property(name = "prop_long")
    var propLong: Long,

    @Property(name = "prop_biginteger")
    @Convert(BigIntegerConverter::class)
    var propBiginteger: BigInteger,

    @Property(name = "prop_double")
    var propDouble: Double,

    @Property(name = "prop_bigdecimal")
    @Convert(BigDecimalConverter::class)
    var propBigdecimal: BigDecimal,
) : Neo4JNode {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NumberPropsNode

        if (propInteger != other.propInteger) return false
        if (propLong != other.propLong) return false
        if (propDouble.compareTo(other.propDouble) != 0) return false
        if (id != other.id) return false
        if (propBiginteger != other.propBiginteger) return false
        if (propBigdecimal.compareTo(other.propBigdecimal) != 0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = propInteger
        result = 31 * result + propLong.hashCode()
        result = 31 * result + propDouble.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + propBiginteger.hashCode()
        result = 31 * result + propBigdecimal.hashCode()
        return result
    }
}
