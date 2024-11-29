package com.thomas.management.domain.adapter

import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.exception.UserNotFoundException
import java.util.UUID

abstract class UserBaseAdapter(
    protected val userRepository: UserRepository,
) {

    protected suspend fun findSimpleByIdOrThrows(
        id: UUID,
    ): UserEntity = userRepository.simple(id, currentOrganization)
        ?: throw UserNotFoundException(id)

}