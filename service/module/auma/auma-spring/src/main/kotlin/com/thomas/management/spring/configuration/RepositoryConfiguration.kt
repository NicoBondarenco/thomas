package com.thomas.management.spring.configuration

import com.thomas.management.data.exposed.repository.GroupExposedRepository
import com.thomas.management.data.exposed.repository.UserExposedRepository
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.UserRepository
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration {

    @Bean
    fun userRepository(
        @Qualifier("management-database") database: Database,
    ): UserRepository = UserExposedRepository(database)

    @Bean
    fun groupRepository(
        @Qualifier("management-database") database: Database,
    ): GroupRepository = GroupExposedRepository(database)

}
