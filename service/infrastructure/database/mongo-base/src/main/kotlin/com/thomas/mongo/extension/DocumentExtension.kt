package com.thomas.mongo.extension

import com.thomas.core.model.entity.BaseEntity
import com.thomas.mongo.configuration.parser.BaseEntityKParameterParser
import com.thomas.mongo.configuration.parser.BaseEntityListKParameterParser
import com.thomas.mongo.configuration.parser.BigDecimalKParameterParser
import com.thomas.mongo.configuration.parser.BigIntegerKParameterParser
import com.thomas.mongo.configuration.parser.BooleanKParameterParser
import com.thomas.mongo.configuration.parser.DoubleKParameterParser
import com.thomas.mongo.configuration.parser.EnumKParameterParser
import com.thomas.mongo.configuration.parser.GenericListKParameterParser
import com.thomas.mongo.configuration.parser.IntKParameterParser
import com.thomas.mongo.configuration.parser.KParameterParser
import com.thomas.mongo.configuration.parser.LocalDateKParameterParser
import com.thomas.mongo.configuration.parser.LocalDateTimeKParameterParser
import com.thomas.mongo.configuration.parser.LocalTimeKParameterParser
import com.thomas.mongo.configuration.parser.LongKParameterParser
import com.thomas.mongo.configuration.parser.MongoParameterParserException
import com.thomas.mongo.configuration.parser.OffsetDateTimeKParameterParser
import com.thomas.mongo.configuration.parser.StringKParameterParser
import com.thomas.mongo.configuration.parser.UUIDKParameterParser
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.Date
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import org.bson.Document
import org.bson.types.Decimal128

private val parsers = listOf<KParameterParser<*>>(
    BaseEntityKParameterParser(),
    BaseEntityListKParameterParser(),
    BigDecimalKParameterParser(),
    BigIntegerKParameterParser(),
    BooleanKParameterParser(),
    DoubleKParameterParser(),
    EnumKParameterParser(),
    GenericListKParameterParser(),
    IntKParameterParser(),
    LocalDateKParameterParser(),
    LocalDateTimeKParameterParser(),
    LocalTimeKParameterParser(),
    LongKParameterParser(),
    OffsetDateTimeKParameterParser(),
    StringKParameterParser(),
    UUIDKParameterParser(),
)

fun Document.getUUIDOrNull(key: String): UUID? = (this[key] as String?)?.let { UUID.fromString(it) }

fun Document.getStringOrNull(key: String): String? = this[key] as String?

fun Document.getBooleanOrNull(key: String): Boolean? = this[key] as Boolean?

fun Document.getIntegerOrNull(key: String): Int? = this[key] as Int?

fun Document.getLongOrNull(key: String): Long? = this[key] as Long?

fun Document.getDoubleOrNull(key: String): Double? = this[key] as Double?

fun Document.getBigDecimalOrNull(key: String): BigDecimal? = (this[key] as Decimal128?)?.bigDecimalValue()

fun Document.getBigIntegerOrNull(key: String): BigInteger? = (this[key] as Decimal128?)?.bigDecimalValue()?.toBigIntegerExact()

fun Document.getLocalDateOrNull(key: String): LocalDate? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalDate()

fun Document.getLocalTimeOrNull(key: String): LocalTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalTime()

fun Document.getLocalDateTimeOrNull(key: String): LocalDateTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalDateTime()

fun Document.getOffsetDateTimeOrNull(key: String): OffsetDateTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toOffsetDateTime()

fun Document.getDocumentOrNull(key: String): Document? = (this[key] as Document?)

fun Document.getEnumOrNull(key: String, klass: Class<Enum<*>>): Enum<*>? = (this[key] as String?)?.let { value ->
    klass.enumConstants.firstOrNull { it.name == value }
}

fun Document.entityClass(): KClass<BaseEntity<*>>? = this.getStringOrNull(ENTITY_CLASS_PROPERTY)?.let {
    Class.forName(it).kotlin as KClass<BaseEntity<*>>
}

fun Document.toEntity(): BaseEntity<*>? = this.entityClass()?.let { this.toEntity(it) }

fun Document.toEntity(klass: KClass<BaseEntity<*>>): BaseEntity<*> {
    val constructor = klass.primaryConstructor!!
    val args = constructor.parameters.map { param ->
        param.getValueFromDocument(this)
    }
    return constructor.call(*args.toTypedArray())
}

private fun KParameter.getValueFromDocument(document: Document): Any? {
    val parser = parsers.firstOrNull { it.accept(this) }
        ?: throw MongoParameterParserException("Type ${this.type.jvmErasure.qualifiedName} is not supported.")
    return parser.parse(this, document)
}
