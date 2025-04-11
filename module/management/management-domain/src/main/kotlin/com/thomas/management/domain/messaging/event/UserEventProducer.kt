package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.user.UserManagementEvent

interface UserEventProducer {

    suspend fun userCreated(event: UserManagementEvent)

    suspend fun userUpdated(event: UserManagementEvent)

}
