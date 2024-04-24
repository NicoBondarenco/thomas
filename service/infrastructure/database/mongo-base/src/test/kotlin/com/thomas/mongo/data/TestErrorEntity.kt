package com.thomas.mongo.data

import com.thomas.core.model.entity.BaseEntity
import java.io.File
import java.util.UUID
import java.util.UUID.randomUUID

data class TestErrorEntity(
    override val id: UUID = randomUUID(),
    val stringValue: String = randomUUID().toString(),
    val fileValue: File = File("test-file.txt"),
) : BaseEntity<TestErrorEntity>()
