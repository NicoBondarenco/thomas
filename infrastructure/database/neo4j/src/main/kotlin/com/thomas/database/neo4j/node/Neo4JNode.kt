package com.thomas.database.neo4j.node

import org.neo4j.ogm.annotation.NodeEntity

interface Neo4JNode {

    fun nodeName(): String = this::class.java.getAnnotation(NodeEntity::class.java).value

}