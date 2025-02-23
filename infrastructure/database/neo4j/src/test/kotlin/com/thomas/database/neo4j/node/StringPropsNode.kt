package com.thomas.database.neo4j.node

import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NoArgsConstructor
@NodeEntity(value = "StringProps")
data class StringPropsNode(

    @Id
    @Property(name = "id")
    var id: String,

    @Property(name = "prop_name")
    var propName: String

) : Neo4JNode
