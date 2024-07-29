package com.thomas.authentication.domain

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent

interface GroupAuthenticationService {

    fun create(event: GroupUpsertedEvent): GroupAuthenticationEntity

    fun update(event: GroupUpsertedEvent): GroupAuthenticationEntity

    fun delete(event: GroupDeletedEvent)

}
