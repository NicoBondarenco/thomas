package com.thomas.database.neo4j.converter

import java.time.OffsetDateTime
import java.time.ZonedDateTime
import org.neo4j.ogm.typeconversion.AttributeConverter

class OffsetDateTimeConverter : AttributeConverter<OffsetDateTime, ZonedDateTime> {

    override fun toGraphProperty(
        value: OffsetDateTime?
    ): ZonedDateTime? = value?.toZonedDateTime()

    override fun toEntityAttribute(
        value: ZonedDateTime?
    ): OffsetDateTime? = value?.toOffsetDateTime()

}
