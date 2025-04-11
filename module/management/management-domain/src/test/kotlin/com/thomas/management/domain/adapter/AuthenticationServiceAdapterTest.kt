package com.thomas.management.domain.adapter

import com.thomas.management.data.entity.userCompleteEntity
import com.thomas.management.data.entity.userFullEntity
import com.thomas.management.domain.AuthenticationService
import com.thomas.management.domain.exception.InactiveOrganizationException
import com.thomas.management.domain.exception.InactiveUserException
import com.thomas.management.domain.exception.InvalidCredentialException
import com.thomas.management.domain.exception.InvalidRefreshTokenException
import com.thomas.management.domain.mock.hasherMock
import com.thomas.management.domain.mock.tokenizerMock
import com.thomas.management.domain.mock.userInactiveOrganization
import com.thomas.management.domain.mock.userInactiveStatus
import com.thomas.management.domain.mock.userInvalidCredential
import com.thomas.management.domain.mock.userLogin
import com.thomas.management.domain.mock.userNotFound
import com.thomas.management.domain.mock.userRepositoryMock
import com.thomas.management.domain.model.request.LoginRequest
import com.thomas.management.domain.model.request.RefreshTokenRequest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class AuthenticationServiceAdapterTest {

    private val service: AuthenticationService = AuthenticationServiceAdapter(
        hasher = hasherMock,
        tokenizer = tokenizerMock,
        userRepository = userRepositoryMock,
    )

    @BeforeEach
    fun beforeEach() {
        userLogin.clear()
        userInactiveStatus.clear()
        userInactiveOrganization.clear()
        userInvalidCredential.clear()
        userNotFound.clear()
    }

    @Test
    fun `Login success`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)

        assertDoesNotThrow {
            service.login(
                LoginRequest(
                    username = user.mainEmail,
                    password = user.passwordHash,
                )
            )
        }
    }

    @Test
    fun `Login not found`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userNotFound.add(user.id)

        assertThrows<InvalidCredentialException> {
            service.login(
                LoginRequest(
                    username = user.mainEmail,
                    password = user.passwordHash,
                )
            )
        }
    }

    @Test
    fun `Login invalid credentials`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userInvalidCredential.add(user.mainEmail)

        assertThrows<InvalidCredentialException> {
            service.login(
                LoginRequest(
                    username = user.mainEmail,
                    password = user.passwordHash,
                )
            )
        }
    }

    @Test
    fun `Login inactive user`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userInactiveStatus.add(user.mainEmail)

        assertThrows<InactiveUserException> {
            service.login(
                LoginRequest(
                    username = user.mainEmail,
                    password = user.passwordHash,
                )
            )
        }
    }

    @Test
    fun `Login inactive organization`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userInactiveOrganization.add(user.mainEmail)

        assertThrows<InactiveOrganizationException> {
            service.login(
                LoginRequest(
                    username = user.mainEmail,
                    password = user.passwordHash,
                )
            )
        }
    }

    @Test
    fun `Refresh success`() = runTest(StandardTestDispatcher()) {
        val user = userFullEntity
        userLogin.add(user)

        assertDoesNotThrow {
            service.refresh(
                RefreshTokenRequest(
                    refreshToken = "${user.mainEmail}_${user.userOrganization.id}",
                )
            )
        }
    }

    @Test
    fun `Refresh not found`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userNotFound.add(user.id)

        assertThrows<InvalidRefreshTokenException> {
            service.refresh(
                RefreshTokenRequest(
                    refreshToken = "${user.mainEmail}_${user.userOrganization.id}",
                )
            )
        }
    }

    @Test
    fun `Refresh inactive user`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userInactiveStatus.add(user.mainEmail)

        assertThrows<InactiveUserException> {
            service.refresh(
                RefreshTokenRequest(
                    refreshToken = "${user.mainEmail}_${user.userOrganization.id}",
                )
            )
        }
    }

    @Test
    fun `Refresh inactive organization`() = runTest(StandardTestDispatcher()) {
        val user = userCompleteEntity
        userLogin.add(user)
        userInactiveOrganization.add(user.mainEmail)

        assertThrows<InactiveOrganizationException> {
            service.refresh(
                RefreshTokenRequest(
                    refreshToken = "${user.mainEmail}_${user.userOrganization.id}",
                )
            )
        }
    }
}
