package com.thomas.database.neo4j.node

import java.io.Serializable
import org.neo4j.ogm.annotation.NodeEntity

interface Neo4JNode<ID : Serializable> {

    var id: ID

    fun nodeName(): String = this::class.java.getAnnotation(NodeEntity::class.java).value

}