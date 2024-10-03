package com.thomas.management.data.exposed.extension

import com.thomas.core.generator.PersonGenerator
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserGroupsEntity
import com.thomas.management.data.entity.UserEntity
import java.util.UUID
import java.util.UUID.fromString

val userUpsert: UserGroupsEntity
    get() = PersonGenerator.generate().let {
        val id = UUID.randomUUID()
        UserGroupsEntity(
            id = id,
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            profilePhoto = null,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            creatorId = id,
            userRoles = setOf(ROLE_USER_READ, ROLE_GROUP_READ),
            userGroups = groupList,
        )
    }

val userSignup: UserEntity
    get() = PersonGenerator.generate().let {
        val id = UUID.randomUUID()
        UserEntity(
            id = id,
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            profilePhoto = null,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            creatorId = id,
        )
    }

val theAvengersGroup: GroupEntity = GroupEntity(
    id = fromString("dc25382c-6fe3-4ee5-a12e-59c8fd3b5442"),
    groupName = "The Avengers",
    groupDescription = null,
    isActive = true,
    creatorId = fromString("436d12f3-d23e-4346-b847-37188521a968"),
)

val ironManGroup: GroupEntity = GroupEntity(
    id = fromString("1f85e146-32ac-4fdd-a3ae-e97a55978c99"),
    groupName = "Team Iron Man",
    groupDescription = null,
    isActive = true,
    creatorId = fromString("436d12f3-d23e-4346-b847-37188521a968"),
)

val guardiansGalaxyGroup: GroupEntity = GroupEntity(
    id = fromString("096e5d76-b40b-4668-9515-68963d0db7ca"),
    groupName = "Guardians of the Galaxy",
    groupDescription = null,
    isActive = true,
    creatorId = fromString("436d12f3-d23e-4346-b847-37188521a968"),
)

private val groupList: Set<GroupEntity>
    get() = setOf(theAvengersGroup, ironManGroup)
