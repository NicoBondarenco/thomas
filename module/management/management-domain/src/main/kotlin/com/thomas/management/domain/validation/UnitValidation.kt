package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationOrganizationDataMaxUnit
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataDuplicatedDocument
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataDuplicatedName

fun UnitRepository.sameName() = DeferredEntityValidation(
    field = UnitEntity::unitName,
    message = { managementUnitValidationUnitDataDuplicatedName() },
    validate = { !this.hasAnotherWithName(it.id, it.unitOrganization.id, it.unitName) },
    context = VT,
)

fun UnitRepository.sameRegistration() = DeferredEntityValidation(
    field = UnitEntity::documentNumber,
    message = { managementUnitValidationUnitDataDuplicatedDocument() },
    validate = { !this.hasAnotherWithDocument(it.id, it.unitOrganization.id, it.documentNumber) },
    context = VT,
)

fun UnitRepository.maxUnits() = DeferredEntityValidation(
    field = UnitEntity::unitOrganization,
    message = { managementUnitValidationOrganizationDataMaxUnit() },
    validate = { !this.limitReached(it.id, it.unitOrganization.id) },
    context = VT,
)
