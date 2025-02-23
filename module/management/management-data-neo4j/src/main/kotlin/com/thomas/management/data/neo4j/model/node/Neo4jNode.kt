package com.thomas.management.data.neo4j.model.node

import org.neo4j.ogm.annotation.NodeEntity

interface Neo4jNode {

    fun nodeName(): String = this::class.java.getAnnotation(NodeEntity::class.java).value

}