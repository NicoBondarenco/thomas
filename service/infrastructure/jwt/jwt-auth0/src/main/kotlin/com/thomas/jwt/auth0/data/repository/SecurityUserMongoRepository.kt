package com.thomas.jwt.auth0.data.repository

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityRole
import com.thomas.core.model.security.SecurityUser
import com.thomas.jwt.auth0.extension.securityGroupCollectionName
import com.thomas.jwt.auth0.extension.securityUserCollectionName
import com.thomas.jwt.configuration.JWTConfiguration
import com.thomas.mongo.extension.getEnumOrNull
import com.thomas.mongo.extension.getLocalDateOrNull
import com.thomas.mongo.extension.getStringOrNull
import com.thomas.mongo.extension.getUUIDOrNull
import java.util.UUID
import org.bson.Document

class SecurityUserMongoRepository(
    database: MongoDatabase,
    configuration: JWTConfiguration
) {

    private val userCollection = database.getCollection(configuration.securityUserCollectionName())
    private val groupCollectionName = configuration.securityGroupCollectionName()

    companion object {
        private const val ID_FIELD = "_id"
        private const val GROUP_FIELD = "userGroups"
        private const val GROUP_ALIAS = "groupList"
    }

    fun findSecurityUser(id: UUID): SecurityUser? =
        userCollection.aggregate(
            listOf(
                Aggregates.lookup(groupCollectionName, GROUP_FIELD, ID_FIELD, GROUP_ALIAS),
                Aggregates.match(Filters.eq(ID_FIELD, id.toString()))
            )
        ).firstOrNull()?.toSecurityUser()

    private fun Document.toSecurityUser() = SecurityUser(
        userId = this.getUUIDOrNull("id")!!,
        firstName = this.getString("firstName"),
        lastName = this.getString("lastName"),
        mainEmail = this.getString("mainEmail"),
        phoneNumber = this.getStringOrNull("phoneNumber"),
        profilePhoto = this.getStringOrNull("profilePhoto"),
        birthDate = this.getLocalDateOrNull("birthDate"),
        userGender = this.getEnumOrNull("userGender", Gender::class.java as Class<Enum<*>>) as Gender?,
        userProfile = this.getEnumOrNull("userProfile", UserProfile::class.java as Class<Enum<*>>)!! as UserProfile,
        isActive = this.getBoolean("isActive"),
        userRoles = this.getList("userRoles", String::class.java).map { SecurityRole.valueOf(it) },
        userGroups = this.getList("groupList", Document::class.java)
            .filter { it.getBoolean("isActive") }
            .map { it.toSecurityGroup() }
    )

    private fun Document.toSecurityGroup() = SecurityGroup(
        groupId = this.getUUIDOrNull("id")!!,
        groupName = this.getString("groupName"),
        groupRoles = this.getList("groupRoles", String::class.java).map { SecurityRole.valueOf(it) },
    )

}