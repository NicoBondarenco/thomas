package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.management.data.entity.OrganizationEntity
import java.util.UUID

data class OrganizationSearchData(
    override val keyword: String? = null,
    override val isActive: Boolean? = null,
    override val organizationId: UUID? = null,
    override val pageable: PageRequestPeriod,
    override val comparator: Comparator<OrganizationEntity>
) : EntitySearchData<OrganizationEntity>() {

    override fun filterKeyWord(entity: OrganizationEntity): Boolean = this.keyword?.let {
        entity.organizationName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.mainEmail.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.registrationNumber.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.fantasyName?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    override fun entityOrganization(
        entity: OrganizationEntity
    ): UUID = entity.id

}
