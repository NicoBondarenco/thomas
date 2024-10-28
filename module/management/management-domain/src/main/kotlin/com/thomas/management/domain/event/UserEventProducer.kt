package com.thomas.management.domain.event

import com.thomas.management.data.entity.UserEntity

interface UserEventProducer {

    suspend fun userCreated(entity: UserEntity)

}