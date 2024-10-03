package com.thomas.management.spring.controller

import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.model.request.GroupActiveRequest
import com.thomas.management.domain.model.request.GroupCreateRequest
import com.thomas.management.domain.model.request.GroupUpdateRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupPageResponse
import com.thomas.management.spring.configuration.ManagementAPIConfiguration.PRIVATE_API_V1_GROUPS
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_GROUPS)
class GroupController(
    private val groupService: GroupService
) {

    @GetMapping
    fun all(
        @RequestParam("kt") keywordText: String?,
        @RequestParam("ia") isActive: Boolean?,
        @RequestParam("cs") createdStart: OffsetDateTime?,
        @RequestParam("ce") createdEnd: OffsetDateTime?,
        @RequestParam("us") updatedStart: OffsetDateTime?,
        @RequestParam("ue") updatedEnd: OffsetDateTime?,
        pageable: PageRequestData
    ): ResponseEntity<PageResponse<GroupPageResponse>> = ok(
        groupService.page(
            keywordText = keywordText,
            isActive = isActive,
            createdStart = createdStart,
            createdEnd = createdEnd,
            updatedStart = updatedStart,
            updatedEnd = updatedEnd,
            pageable = pageable,
        )
    )

    @GetMapping("/{id}")
    fun one(
        @PathVariable("id") id: UUID
    ): ResponseEntity<GroupDetailResponse> = ok(groupService.one(id))

    @PostMapping
    fun create(
        @RequestBody request: GroupCreateRequest
    ): ResponseEntity<GroupDetailResponse> = groupService.create(request).let {
        created(URI("$PRIVATE_API_V1_GROUPS/${it.id}")).body(it)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: UUID,
        @RequestBody request: GroupUpdateRequest
    ): ResponseEntity<GroupDetailResponse> = accepted().body(groupService.update(id, request))

    @PutMapping("/{id}/active")
    fun active(
        @PathVariable("id") id: UUID,
        @RequestBody request: GroupActiveRequest
    ): ResponseEntity<GroupPageResponse> = accepted().body(groupService.active(id, request))

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: UUID
    ): ResponseEntity<Any> = groupService.delete(id).let { noContent().build() }

}
