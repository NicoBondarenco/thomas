package com.thomas.authentication.domain

import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.management.message.event.UserUpsertedEvent

interface UserAuthenticationService {

    fun create(event: UserUpsertedEvent): UserAuthenticationEntity

    fun update(event: UserUpsertedEvent): UserAuthenticationEntity

}
