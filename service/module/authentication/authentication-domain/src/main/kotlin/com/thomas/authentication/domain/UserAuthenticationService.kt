package com.thomas.authentication.domain

import com.thomas.management.message.event.UserUpsertedEvent

interface UserAuthenticationService {

    fun create(event: UserUpsertedEvent)

    fun update(event: UserUpsertedEvent)

}
