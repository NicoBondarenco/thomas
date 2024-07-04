package com.thomas.management.data.entity

data class UserGroupsEntity(
    val user: UserEntity,
    val groups: List<GroupEntity>
)
