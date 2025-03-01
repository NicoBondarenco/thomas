package com.thomas.database.neo4j.node

import java.util.UUID
import org.neo4j.ogm.annotation.NodeEntity

interface Neo4JNode {

    var id: UUID

    fun nodeName(): String = this::class.java.getAnnotation(NodeEntity::class.java).value

}