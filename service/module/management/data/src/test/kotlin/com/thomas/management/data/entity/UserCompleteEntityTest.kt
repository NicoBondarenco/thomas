package com.thomas.management.data.entity

import com.thomas.core.model.general.Gender
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Suppress("UnusedDataClassCopyResult")
class UserCompleteEntityTest : UserEntityBaseTest<UserCompleteEntity>() {

    override fun entity(
        id: UUID,
        firstName: String,
        lastName: String,
        mainEmail: String,
        documentNumber: String,
        phoneNumber: String?,
        profilePhoto: String?,
        birthDate: LocalDate?,
        userGender: Gender?,
        isActive: Boolean,
        creatorId: UUID,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
    ) = UserCompleteEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        mainEmail = mainEmail,
        documentNumber = documentNumber,
        phoneNumber = phoneNumber,
        profilePhoto = profilePhoto,
        birthDate = birthDate,
        userGender = userGender,
        isActive = isActive,
        creatorId = creatorId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userRoles = setOf(),
        userGroups = setOf()
    )

}
