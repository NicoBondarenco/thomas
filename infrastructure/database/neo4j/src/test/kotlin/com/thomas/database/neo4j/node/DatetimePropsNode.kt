package com.thomas.database.neo4j.node

import com.thomas.database.neo4j.converter.OffsetDateTimeConverter
import com.thomas.database.neo4j.converter.UUIDConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "DatetimeProps")
data class DatetimePropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "offset_datetime")
    @Convert(OffsetDateTimeConverter::class)
    var offsetDatetime: OffsetDateTime,

    @Property(name = "offset_time")
    var offsetTime: OffsetTime,

    @Property(name = "zoned_datetime")
    var zonedDatetime: ZonedDateTime,

    @Property(name = "local_datetime")
    var localDatetime: LocalDateTime,

    @Property(name = "local_date")
    var localDate: LocalDate,

    @Property(name = "local_time")
    var localTime: LocalTime

) : Neo4JNode
