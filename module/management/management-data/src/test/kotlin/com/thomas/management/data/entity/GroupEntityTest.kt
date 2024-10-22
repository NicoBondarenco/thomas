package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.generator.OrganizationGenerator.generateOrganizationEntity
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class GroupEntityTest : EntityValidationTest() {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250
        private const val MIN_DESCRIPTION_SIZE = 5
        private const val MAX_DESCRIPTION_SIZE = 1000

        private const val INVALID_CHARACTER = "Invalid value"
        private const val MIN_SIZE = "Invalid min size"
        private const val MAX_SIZE = "Invalid max size"
    }

    private val invalidLegalExecutions = mutableListOf<InvalidDataInput<GroupEntity, GroupEntity>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        entity.copy(
                            groupName = "${randomString(MAX_NAME_SIZE - 5)}$it",
                        )
                    },
                    property = GroupEntity::groupName,
                    message = managementGroupValidationGroupNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        entity.copy(
                            groupDescription = "${randomString(MAX_DESCRIPTION_SIZE - 5)}$it",
                        )
                    },
                    property = GroupEntity::groupDescription,
                    message = managementGroupValidationGroupDescriptionInvalidValue(),
                )
            )
        }
    }

    private val minSizeExecutions = mutableListOf<InvalidDataInput<GroupEntity, GroupEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    entity.copy(
                        groupName = randomString(MIN_NAME_SIZE - 1),
                    )
                },
                property = GroupEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_DESCRIPTION_SIZE)",
                execution = {
                    entity.copy(
                        groupDescription = randomString(MIN_DESCRIPTION_SIZE - 1),
                    )
                },
                property = GroupEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    private val maxSizeExecutions = mutableListOf<InvalidDataInput<GroupEntity, GroupEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    entity.copy(
                        groupName = randomString(MAX_NAME_SIZE + 1),
                    )
                },
                property = GroupEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_DESCRIPTION_SIZE)",
                execution = {
                    entity.copy(
                        groupDescription = randomString(MAX_DESCRIPTION_SIZE + 1),
                    )
                },
                property = GroupEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> =
        invalidLegalExecutions +
                minSizeExecutions +
                maxSizeExecutions

    private val entity: GroupEntity
        get() = GroupEntity(
            id = randomUUID(),
            groupName = randomString(),
            groupDescription = listOf(null, randomString(500)).random(),
            groupOrganization = generateOrganizationEntity(),
            isActive = listOf(true, false).random(),
            createdAt = now(UTC),
            updatedAt = now(UTC),
        )

    @Test
    fun `Valid Group Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }
}