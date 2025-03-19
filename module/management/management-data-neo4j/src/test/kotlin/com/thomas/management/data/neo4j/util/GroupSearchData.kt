package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.management.data.entity.GroupCompleteEntity
import java.util.UUID

data class GroupSearchData(
    override val keyword: String?,
    override val isActive: Boolean?,
    override val organizationId: UUID,
    override val pageable: PageRequestPeriod,
    override val comparator: Comparator<GroupCompleteEntity>
) : EntitySearchData<GroupCompleteEntity>() {

    override fun filterKeyWord(entity: GroupCompleteEntity): Boolean = this.keyword?.let {
        entity.groupName.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.groupDescription?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    override fun entityOrganization(
        entity: GroupCompleteEntity
    ): UUID = entity.groupOrganization.id

}
