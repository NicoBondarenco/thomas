package com.thomas.database.neo4j.node

import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NoArgsConstructor
@NodeEntity(value = "GenericProps")
data class GenericPropsNode(

    @Id
    @Property(name = "id")
    var id: String,

    @Property(name = "prop_name")
    var propName: String?,

    @Property(name = "prop_value")
    var propValue: Int?,

    ) : Neo4JNode
