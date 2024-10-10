package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.generator.UserGenerator.generateSecurityUser
import com.thomas.core.model.security.SecurityRole.MASTER
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthorizationTest {

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @Test
    fun `When role is required and user does not have it, should throws UnauthorizedUserException`() = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUser().let { user ->
            user.copy(
                userOrganization = user.userOrganization.copy(
                    organizationRoles = setOf()
                ),
                userGroups = user.userGroups.map { group ->
                    group.copy(
                        groupOrganization = group.groupOrganization.copy(
                            organizationRoles = setOf()
                        )
                    )
                }.toSet(),
                userHubs = user.userHubs.map { hub ->
                    hub.copy(
                        hubRoles = setOf()
                    )
                }.toSet()
            )
        }
        val exception = assertThrows<UnauthorizedUserException> {
            authorized(roles = arrayOf(MASTER)) {}
        }
        assertEquals(UnauthorizedUserException().message, exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

}
