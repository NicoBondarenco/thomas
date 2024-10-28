package com.thomas.management.data.repository

import java.util.UUID

interface UserRepository {

    suspend fun hasAnotherWithDocument(id: UUID, organizationId: UUID, documentNumber: String): Boolean

    suspend fun hasAnotherWithEmail(id: UUID, mainEmail: String): Boolean

}