package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedRegistration

fun OrganizationRepository.sameName() = DeferredEntityValidation(
    field = OrganizationEntity::organizationName,
    message = { managementOrganizationValidationOrganizationDataDuplicatedName() },
    validate = { !this.hasAnotherWithName(it.id, it.organizationName) },
    context = VT,
)

fun OrganizationRepository.sameRegistration() = DeferredEntityValidation(
    field = OrganizationEntity::registrationNumber,
    message = { managementOrganizationValidationOrganizationDataDuplicatedRegistration() },
    validate = { !this.hasAnotherWithRegistration(it.id, it.registrationNumber) },
    context = VT,
)
