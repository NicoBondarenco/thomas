package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
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

    private val simpleInvalidLegalExecutions = mutableListOf<InvalidDataInput<GroupSimpleEntity, GroupSimpleEntity>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        groupEntity.copy(
                            groupName = "${randomString(MAX_NAME_SIZE - 5)}$it",
                        )
                    },
                    property = GroupSimpleEntity::groupName,
                    message = managementGroupValidationGroupNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        groupEntity.copy(
                            groupDescription = "${randomString(MAX_DESCRIPTION_SIZE - 5)}$it",
                        )
                    },
                    property = GroupSimpleEntity::groupDescription,
                    message = managementGroupValidationGroupDescriptionInvalidValue(),
                )
            )
        }
    }

    private val simpleMinSizeExecutions = mutableListOf<InvalidDataInput<GroupSimpleEntity, GroupSimpleEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    groupEntity.copy(
                        groupName = randomString(MIN_NAME_SIZE - 1),
                    )
                },
                property = GroupSimpleEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_DESCRIPTION_SIZE)",
                execution = {
                    groupEntity.copy(
                        groupDescription = randomString(MIN_DESCRIPTION_SIZE - 1),
                    )
                },
                property = GroupSimpleEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    private val simpleMaxSizeExecutions = mutableListOf<InvalidDataInput<GroupSimpleEntity, GroupSimpleEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    groupEntity.copy(
                        groupName = randomString(MAX_NAME_SIZE + 1),
                    )
                },
                property = GroupSimpleEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_DESCRIPTION_SIZE)",
                execution = {
                    groupEntity.copy(
                        groupDescription = randomString(MAX_DESCRIPTION_SIZE + 1),
                    )
                },
                property = GroupSimpleEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    private val completeInvalidLegalExecutions = mutableListOf<InvalidDataInput<GroupCompleteEntity, GroupCompleteEntity>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        groupCompleteEntity.copy(
                            groupName = "${randomString(MAX_NAME_SIZE - 5)}$it",
                        )
                    },
                    property = GroupCompleteEntity::groupName,
                    message = managementGroupValidationGroupNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        groupCompleteEntity.copy(
                            groupDescription = "${randomString(MAX_DESCRIPTION_SIZE - 5)}$it",
                        )
                    },
                    property = GroupCompleteEntity::groupDescription,
                    message = managementGroupValidationGroupDescriptionInvalidValue(),
                )
            )
        }
    }

    private val completeMinSizeExecutions = mutableListOf<InvalidDataInput<GroupCompleteEntity, GroupCompleteEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    groupCompleteEntity.copy(
                        groupName = randomString(MIN_NAME_SIZE - 1),
                    )
                },
                property = GroupCompleteEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_DESCRIPTION_SIZE)",
                execution = {
                    groupCompleteEntity.copy(
                        groupDescription = randomString(MIN_DESCRIPTION_SIZE - 1),
                    )
                },
                property = GroupCompleteEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    private val completeMaxSizeExecutions = mutableListOf<InvalidDataInput<GroupCompleteEntity, GroupCompleteEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    groupCompleteEntity.copy(
                        groupName = randomString(MAX_NAME_SIZE + 1),
                    )
                },
                property = GroupCompleteEntity::groupName,
                message = managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_DESCRIPTION_SIZE)",
                execution = {
                    groupCompleteEntity.copy(
                        groupDescription = randomString(MAX_DESCRIPTION_SIZE + 1),
                    )
                },
                property = GroupCompleteEntity::groupDescription,
                message = managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE),
            )
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> =
        simpleInvalidLegalExecutions +
                simpleMinSizeExecutions +
                simpleMaxSizeExecutions +
                completeInvalidLegalExecutions +
                completeMinSizeExecutions +
                completeMaxSizeExecutions

    @Test
    fun `Valid Group Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { groupEntity }
        }
    }
}