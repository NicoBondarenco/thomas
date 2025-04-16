package com.thomas.management.spring.controller

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.UnitService
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.UnitResponse
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_UNIT
import java.net.URI
import java.util.UUID
import org.springframework.http.HttpHeaders.LOCATION
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
@RequestMapping(PRIVATE_API_V1_UNIT)
class UnitController(
    private val unitService: UnitService
) {

    @GetMapping("/{id}")
    suspend fun one(
        @PathVariable id: UUID
    ): ResponseEntity<UnitResponse> = ok(unitService.one(id))

    @GetMapping
    suspend fun page(
        @RequestParam("kt") keywordText: String?,
        @RequestParam("ia") isActive: Boolean?,
        pageable: PageRequestPeriod
    ): ResponseEntity<PageResponse<UnitResponse>> = ok(unitService.page(keywordText, isActive, pageable))

    @PostMapping
    suspend fun create(
        @RequestBody request: UnitUpsertRequest
    ): ResponseEntity<UnitResponse> = unitService.create(request).let {
        created(URI.create("$PRIVATE_API_V1_UNIT/${it.id}"))
            .body(it)
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @RequestBody request: UnitUpsertRequest
    ): ResponseEntity<UnitResponse> = unitService.update(id, request).let {
        accepted()
            .header(LOCATION, "$PRIVATE_API_V1_UNIT/${it.id}")
            .body(it)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(
        @PathVariable id: UUID,
    ): ResponseEntity<Unit> = unitService.delete(id).let {
        noContent().build()
    }

}
