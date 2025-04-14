package com.thomas.management.spring.controller

import com.thomas.management.domain.OrganizationService
import com.thomas.management.domain.model.request.OrganizationUpsertRequest
import com.thomas.management.domain.model.response.OrganizationResponse
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_ORGANIZATION
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_ORGANIZATION)
class OrganizationController(
    private val organizationService: OrganizationService
) {

    @PostMapping
    suspend fun create(
        @RequestBody request: OrganizationUpsertRequest
    ): ResponseEntity<OrganizationResponse> {
        val response = ResponseEntity.ok(organizationService.create(request))
        return response
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @RequestBody request: OrganizationUpsertRequest
    ): ResponseEntity<OrganizationResponse> = ResponseEntity.ok(organizationService.update(id, request))

}
