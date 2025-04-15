package com.thomas.management.spring.controller

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.OrganizationService
import com.thomas.management.domain.model.request.OrganizationUpsertRequest
import com.thomas.management.domain.model.response.OrganizationResponse
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_ORGANIZATION
import java.net.URI
import java.util.UUID
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_ORGANIZATION)
class OrganizationController(
    private val organizationService: OrganizationService
) {

    @GetMapping("/{id}")
    suspend fun one(
        @PathVariable id: UUID
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(organizationService.one(id))

    @GetMapping
    suspend fun page(
        @RequestParam("kt") keywordText: String?,
        @RequestParam("ia") isActive: Boolean?,
        pageable: PageRequestPeriod
    ): ResponseEntity<PageResponse<OrganizationResponse>> =
        ResponseEntity.ok(organizationService.page(keywordText, isActive, pageable))

    @PostMapping
    suspend fun create(
        @RequestBody request: OrganizationUpsertRequest
    ): ResponseEntity<OrganizationResponse> =
        organizationService.create(request).let {
            ResponseEntity
                .created(URI.create("$PRIVATE_API_V1_ORGANIZATION/${it.id}"))
                .body(it)
        }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @RequestBody request: OrganizationUpsertRequest
    ): ResponseEntity<OrganizationResponse> =
        organizationService.update(id, request).let {
            ResponseEntity
                .accepted()
                .header(LOCATION, "$PRIVATE_API_V1_ORGANIZATION/${it.id}")
                .body(it)
        }

}
