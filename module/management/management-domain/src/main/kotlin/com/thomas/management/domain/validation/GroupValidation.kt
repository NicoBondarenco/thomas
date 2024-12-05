package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUnitDataNotFound
import java.util.UUID

fun GroupRepository.sameName() = DeferredEntityValidation<GroupCompleteEntity>(
    field = GroupEntity::groupName,
    message = { managementGroupValidationGroupDataDuplicatedName() },
    validate = { !this.hasAnotherWithName(it.id, it.groupData.groupOrganization.id, it.groupData.groupName) },
    context = VT,
)

fun groupUnitsFound(
    groupUnits: Map<UUID, Set<SecurityUnitRole>>,
) = DeferredEntityValidation<GroupCompleteEntity>(
    field = GroupCompleteEntity::groupUnits,
    message = { entity ->
        managementUserValidationUnitDataNotFound(
            groupUnits.keys.subtract(entity.groupUnits.keys.map { it.id }.toSet())
        )
    },
    validate = { it.groupUnits.size == groupUnits.size },
)
