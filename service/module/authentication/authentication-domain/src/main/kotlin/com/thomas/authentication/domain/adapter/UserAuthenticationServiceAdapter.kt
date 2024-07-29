package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.UserAuthenticationService
import com.thomas.authentication.domain.exception.InvalidPasswordException
import com.thomas.authentication.domain.exception.UserAuthenticationNotFoundException
import com.thomas.authentication.domain.model.extension.changePassword
import com.thomas.authentication.domain.model.extension.defaultPassword
import com.thomas.authentication.domain.model.extension.toUserAuthenticationEntity
import com.thomas.authentication.domain.model.extension.updatedFromEvent
import com.thomas.authentication.domain.model.request.ChangePasswordRequest
import com.thomas.authentication.domain.properties.AuthenticationProperties
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.throws
import com.thomas.hash.Hasher
import com.thomas.management.message.event.UserUpsertedEvent
import java.util.UUID

class UserAuthenticationServiceAdapter(
    private val properties: AuthenticationProperties,
    private val repository: UserAuthenticationRepository,
    private val hasher: Hasher,
) : UserAuthenticationService {

    companion object {
        private val PASSWORD_UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXY".toCharArray().toSet()
        private val PASSWORD_LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxy".toCharArray().toSet()
        private val PASSWORD_NUMBER_CHARACTERS = "0123456789".toCharArray().toSet()
        private val PASSWORD_SPECIAL_CHARACTERS = "!@#\$%&*()_+-=<>:?,.;".toCharArray().toSet()
    }

    override fun create(
        event: UserUpsertedEvent
    ): UserAuthenticationEntity {
        val saltHash = hasher.generateSalt()
        val passwordHash = hasher.hash(event.defaultPassword(), saltHash)
        return event.toUserAuthenticationEntity(passwordHash, saltHash).apply {
            repository.create(this)
        }
    }

    override fun update(
        event: UserUpsertedEvent
    ): UserAuthenticationEntity = event.let {
        findByIdOrThrows(it.id).updatedFromEvent(it)
    }.update()

    fun changePassword(
        request: ChangePasswordRequest
    ) = request.newPassword.let { password ->
        password.validatePassword()
        findByIdOrThrows(currentUser.userId).let {
            it.changePassword(hasher.hash(password, it.passwordSalt))
        }
    }.update()

    private fun findByIdOrThrows(
        id: UUID,
    ): UserAuthenticationEntity = repository.one(id)
        ?: throw UserAuthenticationNotFoundException(id)

    private fun UserAuthenticationEntity.update() = repository.update(this)

    private fun String.validatePassword() {
        this.toCharArray().let { array ->
            listOf(
                array.intersect(PASSWORD_UPPER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_LOWER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_NUMBER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_SPECIAL_CHARACTERS).isNotEmpty(),
                array.size >= properties.minimumPasswordLength,
            )
        }.takeIf { it.contains(false) }?.throws { InvalidPasswordException() }
    }
}
