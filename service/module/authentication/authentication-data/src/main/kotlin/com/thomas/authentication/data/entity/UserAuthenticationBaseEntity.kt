package com.thomas.authentication.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

abstract class UserAuthenticationBaseEntity : BaseEntity<UserAuthenticationBaseEntity>() {

    abstract val firstName: String
    abstract val lastName: String
    abstract val mainEmail: String
    abstract val documentNumber: String
    abstract val phoneNumber: String?
    abstract val profilePhoto: String?
    abstract val birthDate: LocalDate?
    abstract val userGender: Gender?
    abstract val isActive: Boolean
    abstract val creatorId: UUID
    abstract val passwordHash: String
    abstract val passwordSalt: String
    abstract val userRoles: Set<SecurityRole>
    abstract val createdAt: OffsetDateTime
    abstract val updatedAt: OffsetDateTime

    override fun toString(): String {
        return """UserBaseEntity(
        firstName='$firstName',
        lastName='$lastName',
        mainEmail='$mainEmail',
        documentNumber='$documentNumber',
        phoneNumber=$phoneNumber,
        profilePhoto=$profilePhoto,
        birthDate=$birthDate,
        userGender=$userGender,
        isActive=$isActive,
        creatorId=$creatorId,
        createdAt=$createdAt,
        updatedAt=$updatedAt,
        userRoles=${userRoles.joinToString(", ")}
        )
        """.trimIndent()
    }

}
