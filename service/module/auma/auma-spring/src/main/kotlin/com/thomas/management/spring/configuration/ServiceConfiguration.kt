package com.thomas.management.spring.configuration

import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.UserService
import com.thomas.management.domain.adapter.GroupServiceAdapter
import com.thomas.management.domain.adapter.UserServiceAdapter
import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.domain.event.UserEventProducer
import com.thomas.management.domain.properties.UserServiceProperties
import com.thomas.management.spring.properties.ManagementProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {

    @Bean
    fun userServiceProperties(
        managementProperties: ManagementProperties,
    ): UserServiceProperties = managementProperties.service

    @Bean
    fun userService(
        userRepository: UserRepository,
        groupRepository: GroupRepository,
        eventProducer: UserEventProducer,
        serviceProperties: UserServiceProperties,
    ): UserService = UserServiceAdapter(
        userRepository = userRepository,
        groupRepository = groupRepository,
        eventProducer = eventProducer,
        serviceProperties = serviceProperties,
    )

    @Bean
    fun groupService(
        groupRepository: GroupRepository,
        eventProducer: GroupEventProducer,
    ): GroupService = GroupServiceAdapter(
        groupRepository = groupRepository,
        eventProducer = eventProducer,
    )

}
