package com.thomas.management.spring.controller

import com.thomas.management.domain.UserService
import com.thomas.management.domain.model.request.UserSignupRequest
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.spring.configuration.ManagementAPIConfiguration.PRIVATE_API_V1_USERS
import com.thomas.management.spring.configuration.ManagementAPIConfiguration.PUBLIC_API_V1_USERS
import java.net.URI
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PUBLIC_API_V1_USERS)
class UserPublicController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    fun signup(
        @RequestBody request: UserSignupRequest
    ): ResponseEntity<UserPageResponse> = userService.signup(request).let {
        created(URI("$PRIVATE_API_V1_USERS/${it.id}")).body(it)
    }

}
