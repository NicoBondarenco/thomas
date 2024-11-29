package com.thomas.management.data.repository

import com.thomas.management.data.entity.GroupEntity
import java.util.UUID

interface GroupRepository {

    suspend fun allByIds(ids: Set<UUID>, organizationId: UUID): Set<GroupEntity>

}