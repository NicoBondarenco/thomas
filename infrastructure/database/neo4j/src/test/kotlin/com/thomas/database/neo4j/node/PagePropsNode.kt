package com.thomas.database.neo4j.node

import com.thomas.database.neo4j.converter.UUIDConverter
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "PageProps")
data class PagePropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "prop_name")
    var propName: String,

    @Property(name = "prop_value")
    var propValue: Int,

    @Property(name = "prop_datetime")
    var propDatetime: ZonedDateTime,

    ) : Neo4JNode
