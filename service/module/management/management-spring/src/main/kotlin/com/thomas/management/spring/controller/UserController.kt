package com.thomas.management.spring.controller

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.UserService
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.spring.configuration.ManagementAPIConfiguration.PRIVATE_API_V1_USERS
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_USERS)
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun all(
        @RequestParam("kt") keywordText: String?,
        @RequestParam("ia") isActive: Boolean?,
        @RequestParam("cs") createdStart: OffsetDateTime?,
        @RequestParam("ce") createdEnd: OffsetDateTime?,
        @RequestParam("us") updatedStart: OffsetDateTime?,
        @RequestParam("ue") updatedEnd: OffsetDateTime?,
        pageable: PageRequest,
    ): ResponseEntity<PageResponse<UserPageResponse>> = ok(
        userService.page(
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
        @PathVariable("id") id: UUID,
    ): ResponseEntity<UserDetailResponse> = ok(userService.one(id))

    @PostMapping
    fun create(
        @RequestBody request: UserCreateRequest
    ): ResponseEntity<UserDetailResponse> = userService.create(request).let {
        created(URI("$PRIVATE_API_V1_USERS/${it.id}")).body(it)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: UUID,
        @RequestBody request: UserUpdateRequest
    ): ResponseEntity<UserDetailResponse> = accepted().body(
        userService.update(id, request)
    )

    @PatchMapping("/{id}/active")
    fun active(
        @PathVariable("id") id: UUID,
        @RequestBody request: UserActiveRequest
    ): ResponseEntity<UserPageResponse> = accepted().body(userService.active(id, request))


}