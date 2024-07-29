package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import com.thomas.authentication.data.repository.GroupAuthenticationRepository
import com.thomas.authentication.domain.GroupAuthenticationService
import com.thomas.authentication.domain.exception.GroupAuthenticationNotFoundException
import com.thomas.authentication.domain.model.extension.toGroupAuthenticationEntity
import com.thomas.authentication.domain.model.extension.updatedFromEvent
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import java.util.UUID

class GroupAuthenticationServiceAdapter(
    private val repository: GroupAuthenticationRepository,
) : GroupAuthenticationService {

    override fun create(
        event: GroupUpsertedEvent
    ): GroupAuthenticationEntity = event.toGroupAuthenticationEntity().apply {
        repository.create(this)
    }

    override fun update(
        event: GroupUpsertedEvent
    ): GroupAuthenticationEntity = event.let {
        findByIdOrThrows(it.id).updatedFromEvent(it)
    }.apply {
        repository.update(this)
    }

    override fun delete(event: GroupDeletedEvent) {
        repository.delete(event.id)
    }

    private fun findByIdOrThrows(
        id: UUID,
    ): GroupAuthenticationEntity = repository.one(id)
        ?: throw GroupAuthenticationNotFoundException(id)

}
