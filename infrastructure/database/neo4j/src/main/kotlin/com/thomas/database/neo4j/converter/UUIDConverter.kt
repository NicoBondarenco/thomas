package com.thomas.database.neo4j.converter

import com.thomas.core.extension.toUUIDOrNull
import java.util.UUID
import org.neo4j.ogm.typeconversion.AttributeConverter

class UUIDConverter : AttributeConverter<UUID, String> {

    override fun toGraphProperty(
        value: UUID?
    ): String? = value?.toString()

    override fun toEntityAttribute(
        value: String?
    ): UUID? = value?.toUUIDOrNull()

}
