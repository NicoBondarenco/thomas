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
import com.thomas.management.data.entity.value.UnitType
import com.thomas.management.data.extension.LEGAL_NAME_REGEX
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationFantasyNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationFantasyNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationUnitNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationUnitNameInvalidValue
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class UnitEntity(
    override val id: UUID = randomUUID(),
    val unitName: String,
    val fantasyName: String? = null,
    val documentNumber: String,
    val unitType: UnitType,
    val unitOrganization: OrganizationEntity,
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
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
) : BaseEntity<UnitEntity>(), ContactInfo, AddressInfo, BasicInfo {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250
    }

    init {
        validate()
    }

    override fun errorMessage(): String = managementUnitValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<UnitEntity>> =
        listOf<EntityValidation<UnitEntity>>(
            EntityValidation(
                UnitEntity::unitName.name.toSnakeCase(),
                { managementUnitValidationUnitNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
                { it.unitName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
            ),
            EntityValidation(
                UnitEntity::unitName.name.toSnakeCase(),
                { managementUnitValidationUnitNameInvalidValue() },
                { it.unitType.isValidName(it.unitName) }
            ),
            EntityValidation(
                UnitEntity::fantasyName.name.toSnakeCase(),
                { managementUnitValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
                { it.fantasyName == null || it.fantasyName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
            ),
            EntityValidation(
                UnitEntity::fantasyName.name.toSnakeCase(),
                { managementUnitValidationFantasyNameInvalidValue() },
                { it.fantasyName == null || LEGAL_NAME_REGEX.matches(it.fantasyName) }
            ),
            EntityValidation(
                UnitEntity::documentNumber.name.toSnakeCase(),
                { managementUnitValidationDocumentNumberInvalidValue() },
                { it.unitType.isValidDocument(it.documentNumber) }
            ),
        ) + contactInfoValidations() + addressInfoValidations()

}
