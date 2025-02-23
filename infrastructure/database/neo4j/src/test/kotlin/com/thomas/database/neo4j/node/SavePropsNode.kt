package com.thomas.database.neo4j.node

import java.time.ZonedDateTime
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NoArgsConstructor
@NodeEntity(value = "SaveProps")
data class SavePropsNode(

    @Id
    @Property(name = "id")
    var id: String,

    @Property(name = "prop_name")
    var propName: String,

    @Property(name = "prop_value")
    var propValue: Int,

    @Property(name = "prop_datetime")
    var propDatetime: ZonedDateTime,

    ) : Neo4JNode
