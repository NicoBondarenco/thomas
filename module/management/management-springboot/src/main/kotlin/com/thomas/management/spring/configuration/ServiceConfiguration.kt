package com.thomas.management.spring.configuration

import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.PasswordResetRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.AuthenticationService
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.OrganizationService
import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.SignupService
import com.thomas.management.domain.UnitService
import com.thomas.management.domain.UserService
import com.thomas.management.domain.adapter.AuthenticationServiceAdapter
import com.thomas.management.domain.adapter.GroupServiceAdapter
import com.thomas.management.domain.adapter.OrganizationServiceAdapter
import com.thomas.management.domain.adapter.PasswordServiceAdapter
import com.thomas.management.domain.adapter.SignupServiceAdapter
import com.thomas.management.domain.adapter.UnitServiceAdapter
import com.thomas.management.domain.adapter.UserServiceAdapter
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.crypt.Tokenizer
import com.thomas.management.domain.messaging.command.NotificationCommandProducer
import com.thomas.management.domain.messaging.event.GroupEventProducer
import com.thomas.management.domain.messaging.event.OrganizationEventProducer
import com.thomas.management.domain.messaging.event.UnitEventProducer
import com.thomas.management.domain.messaging.event.UserEventProducer
import com.thomas.management.domain.properties.PasswordProperties
import com.thomas.management.domain.properties.SignupProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {

    @Bean
    fun authenticationService(
        hasher: Hasher,
        tokenizer: Tokenizer,
        userRepository: UserRepository,
    ): AuthenticationService = AuthenticationServiceAdapter(
        hasher = hasher,
        tokenizer = tokenizer,
        userRepository = userRepository,
    )

    @Bean
    fun signupService(
        signupRepository: SignupRepository,
        signupProperties: SignupProperties,
        hasher: Hasher,
        organizationEventProducer: OrganizationEventProducer,
        userEventProducer: UserEventProducer,
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
    ): SignupService = SignupServiceAdapter(
        signupRepository = signupRepository,
        signupProperties = signupProperties,
        hasher = hasher,
        organizationEventProducer = organizationEventProducer,
        userEventProducer = userEventProducer,
        organizationRepository = organizationRepository,
        userRepository = userRepository,
    )

    @Bean
    fun passwordService(
        userRepository: UserRepository,
        passwordRepository: PasswordResetRepository,
        hasher: Hasher,
        passwordProperties: PasswordProperties,
        notificationProducer: NotificationCommandProducer,
    ): PasswordService = PasswordServiceAdapter(
        userRepository = userRepository,
        passwordRepository = passwordRepository,
        hasher = hasher,
        passwordProperties = passwordProperties,
        notificationProducer = notificationProducer,
    )

    @Bean
    fun organizationService(
        organizationRepository: OrganizationRepository,
        organizationEventProducer: OrganizationEventProducer,
    ): OrganizationService = OrganizationServiceAdapter(
        organizationRepository = organizationRepository,
        organizationEventProducer = organizationEventProducer,
    )

    @Bean
    fun unitService(
        organizationRepository: OrganizationRepository,
        unitRepository: UnitRepository,
        unitEventProducer: UnitEventProducer,
    ): UnitService = UnitServiceAdapter(
        organizationRepository = organizationRepository,
        unitRepository = unitRepository,
        unitEventProducer = unitEventProducer,
    )

    @Bean
    fun groupService(
        organizationRepository: OrganizationRepository,
        groupRepository: GroupRepository,
        unitRepository: UnitRepository,
        groupProducer: GroupEventProducer,
    ): GroupService = GroupServiceAdapter(
        organizationRepository = organizationRepository,
        groupRepository = groupRepository,
        unitRepository = unitRepository,
        groupProducer = groupProducer,
    )

    @Bean
    fun userService(
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
        groupRepository: GroupRepository,
        unitRepository: UnitRepository,
        userProducer: UserEventProducer,
        hasher: Hasher,
    ): UserService = UserServiceAdapter(
        organizationRepository = organizationRepository,
        userRepository = userRepository,
        groupRepository = groupRepository,
        unitRepository = unitRepository,
        userProducer = userProducer,
        hasher = hasher,
    )

}
