package com.thomas.management.domain.adapter

import com.thomas.core.aspect.MethodLog
import com.thomas.core.extension.asyncSessionContext
import com.thomas.core.extension.throws
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationInvalidEntityErrorMessage
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.SignupService
import com.thomas.management.domain.event.OrganizationEventProducer
import com.thomas.management.domain.event.UserEventProducer
import com.thomas.management.domain.exception.SignupDisabledException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedRegistration
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementSignupValidationSignupDataInvalidData
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail
import com.thomas.management.domain.model.mapper.toSignupEntity
import com.thomas.management.domain.model.mapper.toSignupResponse
import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.properties.SignupProperties
import com.thomas.management.domain.validation.sameEmail
import com.thomas.management.domain.validation.sameName
import com.thomas.management.domain.validation.sameRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SignupServiceAdapter(
    private val signupRepository: SignupRepository,
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val signupProperties: SignupProperties,
    private val hasher: Hasher,
    private val organizationEventProducer: OrganizationEventProducer,
    private val userEventProducer: UserEventProducer,
) : SignupService {

    private val organizationValidations = listOf(
        organizationRepository.sameName(),
        organizationRepository.sameRegistration(),
    )

    private val userValidations = listOf(
        userRepository.sameEmail()
    )

    @MethodLog
    override suspend fun signup(
        request: SignupRequest
    ) = signupProperties.takeIf {
        it.signupEnabled
    }?.let {
        request.toSignupEntity(hasher).let {
            validateEntity(it)
            signupRepository.signup(it)
        }.apply {
            organizationEventProducer.organizationCreated(this.organizationData)
            userEventProducer.userCreated(this.userData)
        }.toSignupResponse()
    } ?: throw SignupDisabledException()

    private suspend fun validateEntity(entity: SignupEntity) = coroutineScope {
        listOf(
            asyncValidation(
                entity.organizationData,
                managementOrganizationValidationInvalidEntityErrorMessage(),
                organizationValidations,
            ),
            asyncValidation(
                entity.userData,
                managementUserValidationInvalidEntityErrorMessage(),
                userValidations,
            ),
        ).awaitAll().filterNotNull().map {
            it.errors
        }.flatten().takeIf {
            it.isNotEmpty()
        }?.throws {
            EntityValidationException(managementSignupValidationSignupDataInvalidData(), it)
        }
    }

    private fun <T : BaseEntity<T>> CoroutineScope.asyncValidation(
        entity: T,
        message: String,
        validations: List<DeferredEntityValidation<T>>,
    ) = this.asyncSessionContext {
        try {
            validations.validate(entity, message)
            null
        } catch (e: EntityValidationException) {
            e
        }
    }

    private fun <K, V> List<Map<K, V>>.flatten(): Map<K, V> = mutableMapOf<K, V>().apply {
        this@flatten.forEach { this.putAll(it) }
    }

}
