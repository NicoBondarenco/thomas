package com.thomas.management.spring.controller

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.UserService
import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_USER
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_USER_PASSWORD
import java.net.URI
import java.util.UUID
import org.springframework.http.HttpHeaders.LOCATION
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
@RequestMapping(PRIVATE_API_V1_USER)
class UserController(
    private val userService: UserService,
    private val passwordService: PasswordService,
) {

    @GetMapping("/{id}")
    suspend fun one(
        @PathVariable id: UUID
    ): ResponseEntity<UserDetailResponse> = ok(userService.one(id))

    @GetMapping
    suspend fun page(
        @RequestParam("kt") keywordText: String?,
        @RequestParam("ia") isActive: Boolean?,
        pageable: PageRequestPeriod
    ): ResponseEntity<PageResponse<UserSimpleResponse>> = ok(userService.page(keywordText, isActive, pageable))

    @PostMapping
    suspend fun create(
        @RequestBody request: UserCreateRequest
    ): ResponseEntity<UserDetailResponse> = userService.create(request).let {
        created(URI.create("$PRIVATE_API_V1_USER/${it.id}"))
            .body(it)
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @RequestBody request: UserUpdateRequest
    ): ResponseEntity<UserDetailResponse> = userService.update(id, request).let {
        accepted()
            .header(LOCATION, "$PRIVATE_API_V1_USER/${it.id}")
            .body(it)
    }

    @PatchMapping(PRIVATE_API_V1_USER_PASSWORD)
    suspend fun changePassword(
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<UserSimpleResponse> = accepted().body(passwordService.changePassword(request))

}
