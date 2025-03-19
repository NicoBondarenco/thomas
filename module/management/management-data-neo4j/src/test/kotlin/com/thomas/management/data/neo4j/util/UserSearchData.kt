package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.management.data.entity.UserCompleteEntity
import java.util.UUID

data class UserSearchData(
    override val keyword: String?,
    override val isActive: Boolean?,
    override val organizationId: UUID,
    override val pageable: PageRequestPeriod,
    override val comparator: Comparator<UserCompleteEntity>
) : EntitySearchData<UserCompleteEntity>() {

    override fun filterKeyWord(entity: UserCompleteEntity): Boolean = this.keyword?.let {
        entity.firstName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.lastName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.mainEmail.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.documentNumber.unaccentedLower().contains(it.unaccentedLower())
    } ?: true

    override fun entityOrganization(
        entity: UserCompleteEntity
    ): UUID = entity.userOrganization.id

}
