package com.thomas.management.domain.adapter

import com.thomas.contract.messaging.management.ManagementEventType.CREATE
import com.thomas.core.aspect.AspectClass
import com.thomas.core.aspect.MethodLog
import com.thomas.core.extension.asyncSessionContext
import com.thomas.core.extension.throws
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationInvalidEntityErrorMessage
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.SignupService
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.exception.SignupDisabledException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementSignupValidationSignupDataInvalidData
import com.thomas.management.domain.messaging.event.OrganizationEventProducer
import com.thomas.management.domain.messaging.event.UserEventProducer
import com.thomas.management.domain.model.mapper.toOrganizationManagementEvent
import com.thomas.management.domain.model.mapper.toSignupEntity
import com.thomas.management.domain.model.mapper.toSignupResponse
import com.thomas.management.domain.model.mapper.toUserManagementEvent
import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.properties.SignupProperties
import com.thomas.management.domain.validation.sameEmailSignup
import com.thomas.management.domain.validation.sameName
import com.thomas.management.domain.validation.sameRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@AspectClass
class SignupServiceAdapter(
    private val signupRepository: SignupRepository,
    private val signupProperties: SignupProperties,
    private val hasher: Hasher,
    private val organizationEventProducer: OrganizationEventProducer,
    private val userEventProducer: UserEventProducer,
    organizationRepository: OrganizationRepository,
    userRepository: UserRepository,
) : SignupService {

    private val organizationValidations = listOf(
        organizationRepository.sameName(),
        organizationRepository.sameRegistration(),
    )

    private val userValidations = listOf(
        userRepository.sameEmailSignup(),
    )

    @MethodLog
    override suspend fun signup(
        request: SignupRequest
    ) = signupProperties.signupEnabled.takeIf { it }?.let {
        request.toSignupEntity(hasher, signupProperties).let {
            validateEntity(it)
            signupRepository.signup(it)
        }.apply {
            organizationEventProducer.organizationCreated(this.organizationData.toOrganizationManagementEvent(CREATE))
            userEventProducer.userCreated(this.userData.toUserManagementEvent(CREATE))
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

    private fun <T : BaseEntity<*>> CoroutineScope.asyncValidation(
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
