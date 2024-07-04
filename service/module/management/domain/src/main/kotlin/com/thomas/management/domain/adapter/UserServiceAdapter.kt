package com.thomas.management.domain.adapter

import com.thomas.core.authorization.authorized
import com.thomas.core.extension.throws
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.entity.UserGroupsEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.UserEventProducer
import com.thomas.management.domain.UserService
import com.thomas.management.domain.exception.GroupListNotFoundException
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationDocumentNumberAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationMainEmailAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationPhoneNumberAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.model.extension.toUserDetailResponse
import com.thomas.management.domain.model.extension.toUserEntity
import com.thomas.management.domain.model.extension.toUserPageResponse
import com.thomas.management.domain.model.extension.toUserUpsertedEvent
import com.thomas.management.domain.model.extension.updateActive
import com.thomas.management.domain.model.extension.updateFromRequest
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.domain.userAllRoles
import com.thomas.management.domain.userCreateRoles
import com.thomas.management.domain.userUpdateRoles
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime
import java.util.UUID

class UserServiceAdapter(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val eventProducer: UserEventProducer,
) : UserService {

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<UserPageResponse> = authorized(roles = userAllRoles) {
        userRepository.page(
            keywordText,
            isActive,
            createdStart,
            createdEnd,
            updatedStart,
            updatedEnd,
            pageable
        )
    }.map { it.toUserPageResponse() }

    override fun one(
        id: UUID
    ): UserDetailResponse = authorized(roles = userAllRoles) {
        findByIdWithGroupsOrThrows(id).toUserDetailResponse()
    }

    override fun create(
        request: UserCreateRequest,
    ): UserDetailResponse = authorized(roles = userCreateRoles) {
        request.toUserEntity().process(
            request.userGroups,
            { user, groups -> userRepository.create(user, groups) },
            { eventProducer.sendCreatedEvent(it) },
        )
    }

    override fun update(
        id: UUID,
        request: UserUpdateRequest,
    ): UserDetailResponse = authorized(roles = userUpdateRoles) {
        findByIdOrThrows(id).updateFromRequest(request).process(
            request.userGroups,
            { user, groups -> userRepository.update(user, groups) },
            { eventProducer.sendUpdatedEvent(it) },
        )
    }

    private fun UserEntity.process(
        groupsIds: List<UUID>,
        save: (UserEntity, List<GroupEntity>) -> UserGroupsEntity,
        produce: (UserUpsertedEvent) -> Unit,
    ): UserDetailResponse = this.let {
        it.validateData()
        val groups = findGroups(groupsIds)
        save(it, groups)
    }.apply {
        produce(this.toUserUpsertedEvent())
    }.toUserDetailResponse()

    override fun active(
        id: UUID,
        request: UserActiveRequest,
    ): UserPageResponse = authorized(roles = userUpdateRoles) {
        findByIdWithGroupsOrThrows(id).updateActive(request).apply {
            userRepository.update(this.user)
            eventProducer.sendUpdatedEvent(this.toUserUpsertedEvent())
        }.user.toUserPageResponse()
    }

    private fun findByIdOrThrows(
        id: UUID,
    ): UserEntity = userRepository.findById(id)
        ?: throw UserNotFoundException(id)

    private fun findByIdWithGroupsOrThrows(
        id: UUID,
    ): UserGroupsEntity = userRepository.findByIdWithGroups(id)
        ?: throw UserNotFoundException(id)

    private fun UserEntity.validateData() = listOf<EntityValidation<UserEntity>>(
        EntityValidation(
            UserEntity::mainEmail.name.toSnakeCase(),
            { managementUserValidationMainEmailAlreadyUsed() },
            { !userRepository.hasAnotherWithSameMainEmail(this.id, this.mainEmail) }
        ),
        EntityValidation(
            UserEntity::documentNumber.name.toSnakeCase(),
            { managementUserValidationDocumentNumberAlreadyUsed() },
            { !userRepository.hasAnotherWithSameDocumentNumber(this.id, this.documentNumber) }
        ),
        EntityValidation(
            UserEntity::phoneNumber.name.toSnakeCase(),
            { managementUserValidationPhoneNumberAlreadyUsed() },
            { !(this.phoneNumber?.let { userRepository.hasAnotherWithSamePhoneNumber(this.id, it) } ?: false) }
        ),
    ).validate(this, managementUserValidationUserDataInvalidData())

    private fun findGroups(
        ids: List<UUID>,
    ): List<GroupEntity> = ids.takeIf { it.isNotEmpty() }?.let {
        groupRepository.findByIds(ids).apply {
            ids.filterNot {
                this.any { group -> group.id == it }
            }.takeIf {
                it.isNotEmpty()
            }?.throws {
                GroupListNotFoundException(it)
            }
        }
    } ?: listOf()

}
