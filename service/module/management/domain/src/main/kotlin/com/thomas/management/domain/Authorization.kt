package com.thomas.management.domain

import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_DELETE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE

val userAllRoles = arrayOf(MASTER, ROLE_USER_READ)
val userCreateRoles = arrayOf(MASTER, ROLE_USER_CREATE)
val userUpdateRoles = arrayOf(MASTER, ROLE_USER_UPDATE)

val groupAllRoles = arrayOf(MASTER, ROLE_GROUP_READ)
val groupOneRoles = arrayOf(MASTER, ROLE_GROUP_READ)
val groupCreateRoles = arrayOf(MASTER, ROLE_GROUP_CREATE)
val groupUpdateRoles = arrayOf(MASTER, ROLE_GROUP_UPDATE)
val groupDeleteRoles = arrayOf(MASTER, ROLE_GROUP_DELETE)
