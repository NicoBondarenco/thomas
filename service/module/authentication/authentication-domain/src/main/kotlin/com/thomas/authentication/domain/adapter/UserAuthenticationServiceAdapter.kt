package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.UserAuthenticationService
import com.thomas.authentication.domain.exception.UserAuthenticationNotFoundException
import com.thomas.authentication.domain.model.extension.defaultPassword
import com.thomas.authentication.domain.model.extension.toUserAuthenticationEntity
import com.thomas.authentication.domain.model.extension.updatedFromEvent
import com.thomas.hash.Hasher
import com.thomas.management.message.event.UserUpsertedEvent
import java.util.UUID

class UserAuthenticationServiceAdapter(
    private val repository: UserAuthenticationRepository,
    private val hasher: Hasher,
) : UserAuthenticationService {

    override fun create(
        event: UserUpsertedEvent
    ) {
        val saltHash = hasher.generateSalt()
        val passwordHash = hasher.hash(event.defaultPassword(), saltHash)
        event.toUserAuthenticationEntity(passwordHash, saltHash).apply {
            repository.create(this)
        }
    }

    override fun update(
        event: UserUpsertedEvent
    ): Unit = event.let {
        findByIdOrThrows(it.id).updatedFromEvent(it)
    }.update()

    private fun findByIdOrThrows(
        id: UUID,
    ): UserAuthenticationEntity = repository.one(id)
        ?: throw UserAuthenticationNotFoundException(id)

    private fun UserAuthenticationEntity.update() {
        repository.update(this)
    }


}
