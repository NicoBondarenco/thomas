package com.thomas.management.data.entity.value

import com.thomas.core.i18n.BundleResolver
import com.thomas.management.data.entity.value.AddressState.AddressStateStringsI18N.managementAddressStateString

enum class AddressState {

    AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO;

    val label: String
        get() = managementAddressStateString(this.name.lowercase())

    private object AddressStateStringsI18N : BundleResolver("strings/management-data") {

        fun managementAddressStateString(name: String): String = formattedMessage("management.model.address-state.$name.label")

    }

}
