package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.group.GroupManagementEvent

interface GroupEventProducer {

    suspend fun groupCreated(event: GroupManagementEvent)

    suspend fun groupUpdated(event: GroupManagementEvent)

    suspend fun groupDeleted(event: GroupManagementEvent)

}
