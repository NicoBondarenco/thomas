package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.management.data.entity.UnitEntity
import java.util.UUID

data class UnitSearchData(
    override val keyword: String? = null,
    override val isActive: Boolean? = null,
    override val organizationId: UUID? = null,
    override val pageable: PageRequestPeriod,
    override val comparator: Comparator<UnitEntity>
) : EntitySearchData<UnitEntity>() {

    override fun filterKeyWord(entity: UnitEntity): Boolean = this.keyword?.let {
        entity.unitName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.mainEmail.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.documentNumber.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.fantasyName?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    override fun entityOrganization(
        entity: UnitEntity
    ): UUID = entity.unitOrganization.id

}
