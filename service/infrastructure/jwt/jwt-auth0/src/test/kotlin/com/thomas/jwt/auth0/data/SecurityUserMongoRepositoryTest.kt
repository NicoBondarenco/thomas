package com.thomas.jwt.auth0.data

import com.mongodb.client.AggregateIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.thomas.jwt.auth0.data.repository.SecurityUserMongoRepository
import com.thomas.jwt.auth0.extension.securityUserCollectionName
import com.thomas.jwt.auth0.util.TestMongoCursor
import com.thomas.jwt.auth0.util.defaultConfiguration
import com.thomas.jwt.auth0.util.foundUser
import com.thomas.jwt.auth0.util.usersDocuments
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.bson.Document
import org.bson.conversions.Bson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class SecurityUserMongoRepositoryTest {

    private val userCollection = mockk<MongoCollection<Document>>().apply {
        every { this@apply.aggregate(any()) } answers {
            val argumentList = it.invocation.args[0] as List<Bson>
            val userId = argumentList[1].toBsonDocument().getDocument("\$match").getString("_id").value
            val documents = usersDocuments.filter { doc -> doc.getString("id") == userId }
            mockk<AggregateIterable<Document>>().apply {
                every { this@apply.iterator() } returns TestMongoCursor(documents)
            }
        }
    }

    private val database = mockk<MongoDatabase>().apply {
        every { this@apply.getCollection(defaultConfiguration.securityUserCollectionName()) } returns userCollection
    }

    private val repository = SecurityUserMongoRepository(
        database = database,
        configuration = defaultConfiguration
    )

    @Test
    fun `Security User found`() {
        val user = repository.findSecurityUser(foundUser.userId)
        assertNotNull(user)
        assertEquals(2, user!!.userRoles.size)
        assertEquals(1, user.userGroups.size)
        assertEquals("Group Active", user.userGroups[0].groupName)
        assertEquals(2, user.userGroups[0].groupRoles.size)
        assertEquals(3, user.currentRoles().size)
    }


    @Test
    fun `Security User not found`() {
        val user = repository.findSecurityUser(UUID.randomUUID())
        assertNull(user)
    }

}