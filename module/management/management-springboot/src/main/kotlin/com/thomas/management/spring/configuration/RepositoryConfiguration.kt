package com.thomas.management.spring.configuration

import com.thomas.management.data.neo4j.repository.GroupNeo4JRepository
import com.thomas.management.data.neo4j.repository.OrganizationNeo4JRepository
import com.thomas.management.data.neo4j.repository.PasswordResetNeo4JRepository
import com.thomas.management.data.neo4j.repository.SignupNeo4JRepository
import com.thomas.management.data.neo4j.repository.UnitNeo4JRepository
import com.thomas.management.data.neo4j.repository.UserNeo4JRepository
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.PasswordResetRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import org.neo4j.ogm.session.SessionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration {


    @Bean
    fun groupRepository(
        sessionFactory: SessionFactory
    ): GroupRepository = GroupNeo4JRepository(sessionFactory)

    @Bean
    fun organizationRepository(
        sessionFactory: SessionFactory
    ): OrganizationRepository = OrganizationNeo4JRepository(sessionFactory)

    @Bean
    fun unitRepository(
        sessionFactory: SessionFactory
    ): UnitRepository = UnitNeo4JRepository(sessionFactory)

    @Bean
    fun passwordResetRepository(
        sessionFactory: SessionFactory
    ): PasswordResetRepository = PasswordResetNeo4JRepository(sessionFactory)

    @Bean
    fun userRepository(
        sessionFactory: SessionFactory
    ): UserRepository = UserNeo4JRepository(sessionFactory)

    @Bean
    fun signupRepository(
        sessionFactory: SessionFactory
    ): SignupRepository = SignupNeo4JRepository(sessionFactory)

}
