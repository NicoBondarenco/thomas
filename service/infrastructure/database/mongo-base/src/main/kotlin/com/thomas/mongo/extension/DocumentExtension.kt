package com.thomas.mongo.extension

import com.thomas.core.model.entity.BaseEntity
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
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import org.bson.Document
import org.bson.types.Decimal128

fun Document.getUUID(key: String): UUID = this.getUUIDOrNull(key)!!
fun Document.getUUIDOrNull(key: String): UUID? = (this[key] as String?)?.let { UUID.fromString(it) }

fun Document.getStringOrNull(key: String): String? = this[key] as String?

fun Document.getBooleanOrNull(key: String): Boolean? = this[key] as Boolean?

fun Document.getIntegerOrNull(key: String): Int? = this[key] as Int?

fun Document.getLongOrNull(key: String): Long? = this[key] as Long?

fun Document.getDoubleOrNull(key: String): Double? = this[key] as Double?

fun Document.getBigDecimal(key: String): BigDecimal = this.getBigDecimalOrNull(key)!!
fun Document.getBigDecimalOrNull(key: String): BigDecimal? = (this[key] as Decimal128?)?.bigDecimalValue()

fun Document.getBigInteger(key: String): BigInteger = this.getBigIntegerOrNull(key)!!
fun Document.getBigIntegerOrNull(key: String): BigInteger? = (this[key] as Decimal128?)?.bigDecimalValue()?.toBigIntegerExact()

fun Document.getLocalDate(key: String): LocalDate = this.getLocalDateOrNull(key)!!
fun Document.getLocalDateOrNull(key: String): LocalDate? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalDate()

fun Document.getLocalTime(key: String): LocalTime = this.getLocalTimeOrNull(key)!!
fun Document.getLocalTimeOrNull(key: String): LocalTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalTime()

fun Document.getLocalDateTime(key: String): LocalDateTime = this.getLocalDateTimeOrNull(key)!!
fun Document.getLocalDateTimeOrNull(key: String): LocalDateTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toLocalDateTime()

fun Document.getOffsetDateTime(key: String): OffsetDateTime = this.getOffsetDateTimeOrNull(key)!!
fun Document.getOffsetDateTimeOrNull(key: String): OffsetDateTime? = (this[key] as Date?)?.toInstant()?.atZone(UTC)?.toOffsetDateTime()

fun Document.getDocument(key: String): Document = this.getDocumentOrNull(key)!!
fun Document.getDocumentOrNull(key: String): Document? = (this[key] as Document?)

fun Document.getEnum(key: String, klass: Class<Enum<*>>): Enum<*> = this.getEnumOrNull(key, klass)!!
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
        this.getValueByClass(param)
    }
    return constructor.call(*args.toTypedArray())
}

@Suppress("IMPLICIT_CAST_TO_ANY")
private fun Document.getValueByClass(
    param: KParameter
): Any? {
    val nullable = param.type.isMarkedNullable
    val key = param.name!!
    val klass = param.type.jvmErasure
    return when (klass) {
        UUID::class -> if (nullable) this.getUUIDOrNull(key) else this.getUUID(key)
        String::class -> if (nullable) this.getStringOrNull(key) else this.getString(key)
        Boolean::class -> if (nullable) this.getBooleanOrNull(key) else this.getBoolean(key)
        Int::class -> if (nullable) this.getIntegerOrNull(key) else this.getInteger(key)
        Long::class -> if (nullable) this.getLongOrNull(key) else this.getLong(key)
        Double::class -> if (nullable) this.getDoubleOrNull(key) else this.getDouble(key)
        BigDecimal::class -> if (nullable) this.getBigDecimalOrNull(key) else this.getBigDecimal(key)
        BigInteger::class -> if (nullable) this.getBigIntegerOrNull(key) else this.getBigInteger(key)
        LocalDate::class -> if (nullable) this.getLocalDateOrNull(key) else this.getLocalDate(key)
        LocalTime::class -> if (nullable) this.getLocalTimeOrNull(key) else this.getLocalTime(key)
        LocalDateTime::class -> if (nullable) this.getLocalDateTimeOrNull(key) else this.getLocalDateTime(key)
        OffsetDateTime::class -> if (nullable) this.getOffsetDateTimeOrNull(key) else this.getOffsetDateTime(key)
        else -> {
            if (klass.java.isEnum) {
                if (nullable) this.getEnumOrNull(key, klass.java as Class<Enum<*>>) else this.getEnum(key, klass.java as Class<Enum<*>>)
            } else if (klass.isSubclassOf(BaseEntity::class)) {
                this.getDocumentOrNull(key)?.toEntity(klass as KClass<BaseEntity<*>>)
            } else if (param.isList()) {
                if (param.isBaseEntityList()) {
                    (this[key] as List<Document>?)?.mapNotNull { it.toEntity() }
                } else {
                    (this[key] as List<*>?)
                }
            } else {
                throw IllegalArgumentException("Type $klass is not supported.")
            }
        }
    }
}

private fun KParameter.isList() =
    this.type.jvmErasure.isSubclassOf(List::class)

private fun KParameter.isBaseEntityList() =
    this.type.arguments.first().type?.jvmErasure?.isSubclassOf(BaseEntity::class) == true