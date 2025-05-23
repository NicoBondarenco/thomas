package com.thomas.management.data.entity

import com.thomas.core.aspect.MaskField
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.Race
import com.thomas.core.model.general.UserType
import com.thomas.core.model.general.UserType.COMMON
import com.thomas.core.model.security.SecurityOrganizationRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class UserSimpleEntity(
    override val id: UUID = randomUUID(),
    override val firstName: String,
    override val lastName: String,
    @MaskField override val documentNumber: String,
    override val profilePhoto: String? = null,
    override val userGender: Gender? = null,
    override val userRace: Race? = null,
    override val userType: UserType = COMMON,
    override val birthDate: LocalDate? = null,
    @MaskField override val passwordSalt: String,
    @MaskField override val passwordHash: String,
    override val userOrganization: OrganizationEntity,
    override val organizationRoles: Set<SecurityOrganizationRole>,
    override val mainEmail: String,
    override val mainPhone: String,
    override val isActive: Boolean = true,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
) : UserEntity() {

    init {
        validate()
    }

}
