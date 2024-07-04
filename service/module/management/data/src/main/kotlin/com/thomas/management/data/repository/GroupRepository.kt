package com.thomas.management.data.repository

import com.thomas.management.data.entity.GroupEntity
import java.util.UUID

interface GroupRepository {

    fun findByIds(ids: List<UUID>): List<GroupEntity>

}
