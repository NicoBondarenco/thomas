package com.thomas.management.data.neo4j.repository

import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.data.entity.signupEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toUserCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toUserSimpleEntity
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UserNode
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.neo4j.ogm.session.SessionFactory

class SignupNeo4JRepositoryTest : ManagementFunSpec<SignupNeo4JRepository>(
    body = {
        initNodes(
            mapOf(
                UserNode::class to { (it as UserNode).toUserCompleteEntity() },
                OrganizationNode::class to { (it as OrganizationNode).toOrganizationEntity() },
            )
        )

        context(name = "Signup") {
            val data: Map<String, SignupEntity> = (1..5).map {
                signupEntity.copy()
            }.associateBy {
                it.userData.id.toString()
            }

            withData(data) { signup ->
                repository.signup(signup)

                val resultUser = sessionFactory.openSession().load(UserNode::class.java, signup.userData.id, 5).toUserSimpleEntity()
                val resultOrganization = sessionFactory.openSession().load(OrganizationNode::class.java, signup.organizationData.id, 5).toOrganizationEntity()

                resultUser.id shouldBe signup.userData.id
                resultUser.firstName shouldBe signup.userData.firstName
                resultUser.lastName shouldBe signup.userData.lastName
                resultUser.documentNumber shouldBe signup.userData.documentNumber
                resultUser.profilePhoto shouldBe signup.userData.profilePhoto
                resultUser.userGender shouldBe signup.userData.userGender
                resultUser.birthDate shouldBe signup.userData.birthDate
                resultUser.passwordSalt shouldBe signup.userData.passwordSalt
                resultUser.passwordHash shouldBe signup.userData.passwordHash
                resultUser.userOrganization shouldBe signup.userData.userOrganization
                resultUser.isActive shouldBe signup.userData.isActive
                resultUser.createdAt shouldBe signup.userData.createdAt
                resultUser.updatedAt shouldBe signup.userData.updatedAt

                resultOrganization shouldBe signup.organizationData
            }
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): SignupNeo4JRepository = SignupNeo4JRepository(sessionFactory)

}
