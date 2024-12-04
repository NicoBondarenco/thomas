package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.AddressStateEvent
import com.thomas.contract.messaging.management.UnitTypeEvent
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import kotlinx.coroutines.coroutineScope

suspend fun AddressState.toAddressStateEvent() = coroutineScope {
    AddressStateEvent.valueOf(this@toAddressStateEvent.name)
}

suspend fun UnitType.toUnitTypeEvent() = coroutineScope {
    UnitTypeEvent.valueOf(this@toUnitTypeEvent.name)
}
