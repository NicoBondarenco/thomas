package com.thomas.management.domain.adapter

import com.thomas.contract.messaging.ApplicationEventType.CREATE
import com.thomas.contract.messaging.ApplicationEventType.UPDATE
import com.thomas.core.aspect.AspectClass
import com.thomas.core.aspect.MethodLog
import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.validate
import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.general.UserType
import com.thomas.core.model.general.UserType.COMMON
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.UserService
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.messaging.event.UserEventProducer
import com.thomas.management.domain.model.mapper.toUnitRoleEntity
import com.thomas.management.domain.model.mapper.toUserCompleteEntity
import com.thomas.management.domain.model.mapper.toUserDetailResponse
import com.thomas.management.domain.model.mapper.toUserManagementEvent
import com.thomas.management.domain.model.mapper.toUserSimpleResponse
import com.thomas.management.domain.model.mapper.updateFromRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import com.thomas.management.domain.userCreateRoles
import com.thomas.management.domain.userReadRoles
import com.thomas.management.domain.userUpdateRoles
import com.thomas.management.domain.validation.maxUsers
import com.thomas.management.domain.validation.sameDocument
import com.thomas.management.domain.validation.sameEmail
import com.thomas.management.domain.validation.userGroupsFound
import com.thomas.management.domain.validation.userUnitsFound
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@AspectClass
class UserServiceAdapter(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val unitRepository: UnitRepository,
    private val userProducer: UserEventProducer,
    private val hasher: Hasher,
) : UserService {

    companion object {
        private val USER_TYPE_ALLOWED = listOf(COMMON)
    }

    private fun userCreateValidations(
        userGroups: Set<UUID>,
        userUnits: Map<UUID, Set<SecurityUnitRole>>,
    ) = userUpdateValidations(userGroups, userUnits) + userRepository.maxUsers()

    private fun userUpdateValidations(
        userGroups: Set<UUID>,
        userUnits: Map<UUID, Set<SecurityUnitRole>>,
    ) = listOf(
        userRepository.sameEmail(),
        userRepository.sameDocument(),
        userGroupsFound(userGroups),
        userUnitsFound(userUnits),
    )

    private fun CoroutineScope.organizationDeferred() = async {
        organizationRepository.one(currentOrganization)!!
    }

    private fun CoroutineScope.passwordDeferred(request: UserCreateRequest) = async {
        val initialPassword = request.let {
            "${it.firstName.trim().first()}${it.lastName.trim().first()}@${it.documentNumber.substring(0, 6)}"
        }
        hasher.generateSalt().let {
            it to hasher.hash(initialPassword, it)
        }
    }

    private fun CoroutineScope.groupsDeferred(request: UserRequest) = async {
        groupRepository.allFullByIds(request.userGroups, currentOrganization)
    }

    private fun CoroutineScope.unitsDeferred(request: UserRequest) = async {
        val found = unitRepository.allByIds(request.userUnits.keys, currentOrganization)
        found.associateWith { request.userUnits[it.id]!!.toSet() }.toUnitRoleEntity()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun UserCreateRequest.toCompleteEntity(): UserCompleteEntity = coroutineScope {
        val userOrganization = organizationDeferred()
        val userGroups = groupsDeferred(this@toCompleteEntity)
        val userUnits = unitsDeferred(this@toCompleteEntity)
        val userPassword = passwordDeferred(this@toCompleteEntity)

        listOf(
            userOrganization,
            userGroups,
            userUnits,
            userPassword,
        ).awaitAll()

        userPassword.getCompleted().let {
            this@toCompleteEntity.toUserCompleteEntity(
                userOrganization = userOrganization.getCompleted(),
                it.first,
                it.second,
                userGroups.getCompleted(),
                userUnits.getCompleted(),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun UserUpdateRequest.toCompleteEntity(id: UUID): UserCompleteEntity = coroutineScope {
        findCompleteByIdOrThrows(id).let { userEntity ->

            val userGroups = groupsDeferred(this@toCompleteEntity)
            val userUnits = unitsDeferred(this@toCompleteEntity)

            listOf(
                userGroups,
                userUnits,
            ).awaitAll()

            userEntity.updateFromRequest(
                this@toCompleteEntity,
                userGroups.getCompleted(),
                userUnits.getCompleted(),
            )
        }
    }

    private suspend fun UserCompleteEntity.upsert(
        userValidations: List<DeferredEntityValidation<UserCompleteEntity>>,
        upsert: suspend (UserCompleteEntity) -> UserCompleteEntity,
        produce: suspend (UserCompleteEntity) -> Unit,
    ) = this.let {
        userValidations.validate(it, managementUserValidationUserDataInvalidData())
        upsert(it)
    }.apply {
        produce(this)
    }.toUserDetailResponse()

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod,
    ): PageResponse<UserSimpleResponse> = authorized(userReadRoles) {
        userRepository.page(
            keywordText = keywordText,
            isActive = isActive,
            userTypes = UserType.entries.takeIf { currentUser.isAdministrator } ?: USER_TYPE_ALLOWED,
            organizationId = currentOrganization,
            pageable = pageable,
        ).map { it.toUserSimpleResponse() }
    }

    override suspend fun one(
        id: UUID
    ): UserDetailResponse = authorized(userReadRoles) {
        findCompleteByIdOrThrows(id).toUserDetailResponse()
    }

    @MethodLog
    override suspend fun create(
        request: UserCreateRequest,
    ): UserDetailResponse = authorized(userCreateRoles) {
        request.toCompleteEntity().upsert(
            userCreateValidations(
                userGroups = request.userGroups,
                userUnits = request.userUnits,
            ),
            { userRepository.create(it) },
            { userProducer.userCreated(it.toUserManagementEvent(CREATE)) }
        )
    }

    @MethodLog
    override suspend fun update(
        id: UUID,
        request: UserUpdateRequest,
    ): UserDetailResponse = authorized(userUpdateRoles) {
        request.toCompleteEntity(id).upsert(
            userUpdateValidations(
                userGroups = request.userGroups,
                userUnits = request.userUnits,
            ),
            { userRepository.update(it) },
            { userProducer.userUpdated(it.toUserManagementEvent(UPDATE)) },
        )
    }

    private suspend fun findCompleteByIdOrThrows(
        id: UUID,
    ): UserCompleteEntity = userRepository.one(
        id,
        currentOrganization,
        UserType.entries.takeIf { currentUser.isAdministrator } ?: USER_TYPE_ALLOWED
    ) ?: throw UserNotFoundException(id)

}
