package com.thomas.management.spring.configuration

import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.UserService
import com.thomas.management.domain.adapter.GroupServiceAdapter
import com.thomas.management.domain.adapter.UserServiceAdapter
import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.domain.event.UserEventProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {

    @Bean
    fun userService(
        userRepository: UserRepository,
        groupRepository: GroupRepository,
        eventProducer: UserEventProducer
    ): UserService = UserServiceAdapter(
        userRepository = userRepository,
        groupRepository = groupRepository,
        eventProducer = eventProducer,
    )

    @Bean
    fun groupService(
        groupRepository: GroupRepository,
        eventProducer: GroupEventProducer
    ): GroupService = GroupServiceAdapter(
        groupRepository = groupRepository,
        eventProducer = eventProducer,
    )

}
