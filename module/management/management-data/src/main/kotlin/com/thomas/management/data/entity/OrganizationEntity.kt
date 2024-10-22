package com.thomas.management.data.entity

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE
import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.management.data.entity.info.AddressInfo
import com.thomas.management.data.entity.info.BasicInfo
import com.thomas.management.data.entity.info.ContactInfo
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.extension.isValidRegistrationNumber
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationFantasyNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationFantasyNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationOrganizationNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationOrganizationNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationRegistrationNumberInvalidValue
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class OrganizationEntity(
    override val id: UUID = randomUUID(),
    val organizationName: String,
    val fantasyName: String? = null,
    val registrationNumber: String,
    override val mainEmail: String,
    override val mainPhone: String,
    override val addressZipcode: String,
    override val addressStreet: String,
    override val addressNumber: String,
    override val addressComplement: String? = null,
    override val addressNeighborhood: String,
    override val addressCity: String,
    override val addressState: AddressState,
    override val isActive: Boolean = true,
    override val createAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
) : BaseEntity<OrganizationEntity>(), ContactInfo, AddressInfo, BasicInfo {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250
        private val ORGANIZATION_NAME_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-. ]+".toRegex()
    }

    init {
        validate()
    }

    override fun errorMessage(): String = managementOrganizationValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<OrganizationEntity>> =
        listOf<EntityValidation<OrganizationEntity>>(
            EntityValidation(
                OrganizationEntity::organizationName.name.toSnakeCase(),
                { managementOrganizationValidationOrganizationNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
                { it.organizationName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
            ),
            EntityValidation(
                OrganizationEntity::organizationName.name.toSnakeCase(),
                { managementOrganizationValidationOrganizationNameInvalidValue() },
                { ORGANIZATION_NAME_REGEX.matches(it.organizationName) }
            ),
            EntityValidation(
                OrganizationEntity::fantasyName.name.toSnakeCase(),
                { managementOrganizationValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
                { it.fantasyName == null || it.fantasyName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
            ),
            EntityValidation(
                OrganizationEntity::fantasyName.name.toSnakeCase(),
                { managementOrganizationValidationFantasyNameInvalidValue() },
                { it.fantasyName == null || ORGANIZATION_NAME_REGEX.matches(it.fantasyName) }
            ),
            EntityValidation(
                OrganizationEntity::registrationNumber.name.toSnakeCase(),
                { managementOrganizationValidationRegistrationNumberInvalidValue() },
                { it.registrationNumber.isValidRegistrationNumber() }
            ),
        ) + contactInfoValidations() + addressInfoValidations()

}
