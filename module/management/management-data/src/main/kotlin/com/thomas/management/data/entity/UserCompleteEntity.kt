package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID

data class UserCompleteEntity(
    val userData: UserEntity,
    val userGroups: Set<GroupEntity>,
    val userUnits: Map<UnitEntity, Set<SecurityUnitRole>>,
) : BaseEntity<UserCompleteEntity>() {

    override val id: UUID = userData.id

}
