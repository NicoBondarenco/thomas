package com.thomas.authentication.event

import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.core.generator.PersonGenerator
import com.thomas.core.random.randomSecurityRoles
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

val userAuthentication: UserAuthenticationEntity
    get() = PersonGenerator.generate().let {
        UserAuthenticationEntity(
            id = randomUUID(),
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            profilePhoto = randomUUID().toString(),
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            passwordHash = randomUUID().toString(),
            passwordSalt = randomUUID().toString(),
            userRoles = setOf(),
            createdAt = now(UTC),
            updatedAt = now(UTC),
            groupsIds = setOf(),
        )
    }

val userCompleteAuthentication: UserAuthenticationCompleteEntity
    get() = PersonGenerator.generate().let {
        UserAuthenticationCompleteEntity(
            id = randomUUID(),
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            profilePhoto = randomUUID().toString(),
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            passwordHash = randomUUID().toString(),
            passwordSalt = randomUUID().toString(),
            userRoles = randomSecurityRoles().toSet(),
            createdAt = now(UTC),
            updatedAt = now(UTC),
            userGroups = setOf(groupAuthenticationEntity),
        )
    }

val userUpsertedEvent: UserUpsertedEvent
    get() = PersonGenerator.generate().let {
        UserUpsertedEvent(
            id = randomUUID(),
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            profilePhoto = randomUUID().toString(),
            userRoles = setOf(),
            userGroups = setOf(),
            createdAt = now(UTC),
            updatedAt = now(UTC),
        )
    }
