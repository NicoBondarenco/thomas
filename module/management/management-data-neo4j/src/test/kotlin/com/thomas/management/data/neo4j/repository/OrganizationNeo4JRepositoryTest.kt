package com.thomas.management.data.neo4j.repository

import com.thomas.management.data.entity.organizationEntity
import com.thomas.management.data.entity.value.AddressState.SP
import com.thomas.management.data.neo4j.model.mapper.toOrganizationNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.util.runScript
import java.time.OffsetDateTime
import java.util.UUID
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OrganizationNeo4JRepositoryTest : Neo4JRepositoryTest() {

    @Test
    fun `Save entity`() = runTest(StandardTestDispatcher()) {
        val organization = organizationEntity
        organizationRepository.create(organization)
        val result = sessionFactory.openSession().load(OrganizationNode::class.java, organization.id.toString())
        assertEquals(organization.toOrganizationNode(), result)
    }

    @Test
    fun `Update entity`() = runTest(StandardTestDispatcher()) {
        val id = "390f9ada-02de-4113-adfb-05419a7a5f90"
        sessionFactory.runScript("organization/update")

        val organization = organizationEntity.copy(
            id = UUID.fromString(id),

            )
        organizationRepository.update(organization)

        val result = sessionFactory.openSession().load(OrganizationNode::class.java, id)
        assertEquals(organization.toOrganizationNode(), result)
    }

    @Test
    fun `Find entity`() = runTest(StandardTestDispatcher()) {
        val id = UUID.fromString("9fcb8a06-3652-4514-b523-1e604a8485a4")
        sessionFactory.runScript("organization/one")

        val organization = organizationEntity.copy(
            id = id,
            organizationName = "Organization Name",
            fantasyName = "Fantasy Name",
            registrationNumber = "01589560000196",
            maximumUsers = 7,
            maximumUnits = 7,
            mainEmail = "fake.email@email.com",
            mainPhone = "16988776655",
            addressZipcode = "52068421",
            addressStreet = "Fake Street",
            addressNumber = "100",
            addressComplement = "Second Floor",
            addressNeighborhood = "Fake Neighborhood",
            addressCity = "Fake City",
            addressState = SP,
            isActive = true,
            createdAt = OffsetDateTime.parse("2025-01-01T03:33:47.996854Z"),
            updatedAt = OffsetDateTime.parse("2025-02-04T19:01:13.016698Z"),
        )
        val result = organizationRepository.one(id)

        assertEquals(organization, result)
    }

    @Test
    fun `Same name exists`() = runTest(StandardTestDispatcher()) {
        sessionFactory.runScript("organization/one-with")
        assertTrue(organizationRepository.hasAnotherWithName(UUID.randomUUID(), "Organization Unique"))
    }


}