package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail

fun UserRepository.sameEmail() = DeferredEntityValidation(
    field = UserEntity::mainEmail,
    message = { managementUserValidationUserDataDuplicatedEmail() },
    validate = { !this.hasAnotherWithEmail(it.id, it.mainEmail) },
    context = VT,
)
