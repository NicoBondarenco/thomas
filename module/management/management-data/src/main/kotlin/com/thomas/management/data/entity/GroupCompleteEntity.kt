package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID

data class GroupCompleteEntity(
    val groupData: GroupEntity,
    val groupUnits: Map<UnitEntity, Set<SecurityUnitRole>>,
) : BaseEntity<GroupCompleteEntity>() {

    override val id: UUID = groupData.id

}
