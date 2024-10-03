package com.thomas.mongodb.sync.data

import com.thomas.core.model.entity.BaseEntity
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean

data class ParentTestEntity(
    override val id: UUID = randomUUID(),
    val parentName: String = randomUUID().toString(),
    val childIds: List<UUID> = listOf(randomUUID(), randomUUID()),
) : BaseEntity<ParentTestEntity>()

data class ChildTestEntity(
    override val id: UUID = randomUUID(),
    val childName: String = randomUUID().toString(),
    val isActive: Boolean = nextBoolean(),
) : BaseEntity<ChildTestEntity>()

data class FullTestEntity(
    val parentId: UUID,
    val parentName: String,
    val childList: List<ChildTestEntity>,
)

val childTestList = listOf(
    ChildTestEntity(isActive = true),
    ChildTestEntity(isActive = true),
    ChildTestEntity(isActive = false),
)

val parentTestEntity = ParentTestEntity(
    childIds = childTestList.map { it.id }
)

val fullTestEntity = FullTestEntity(
    parentId = parentTestEntity.id,
    parentName = parentTestEntity.parentName,
    childList = childTestList.filter { it.isActive }
)
