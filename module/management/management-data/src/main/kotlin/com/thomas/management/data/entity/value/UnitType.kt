package com.thomas.management.data.entity.value

import com.thomas.core.i18n.BundleResolver
import com.thomas.management.data.entity.value.UnitType.UnitTypeStringsI18N.managementUnitTypeString
import com.thomas.management.data.extension.LEGAL_NAME_REGEX
import com.thomas.management.data.extension.NATURAL_NAME_REGEX
import com.thomas.management.data.extension.isValidDocumentNumber
import com.thomas.management.data.extension.isValidRegistrationNumber

enum class UnitType(
    val isValidName: (String) -> Boolean,
    val isValidDocument: (String) -> Boolean,
) {

    NATURAL(
        { NATURAL_NAME_REGEX.matches(it) },
        { it.isValidDocumentNumber() },
    ),
    LEGAL(
        { LEGAL_NAME_REGEX.matches(it) },
        { it.isValidRegistrationNumber() },
    );

    val label: String
        get() = managementUnitTypeString(this.name.lowercase())

    private object UnitTypeStringsI18N : BundleResolver("strings/management-data") {

        fun managementUnitTypeString(name: String): String = formattedMessage("management.model.unit-type.$name.label")

    }

}