/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.controller

import co.brainz.api.ApiUtil
import co.brainz.api.workflow.service.ApiWorkflowService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/wf")
class ApiWorkflowController(
    private val apiWorkflowService: ApiWorkflowService
) : ApiUtil() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{documentId}/form-data")
    fun getDocumentDataStructure(
        request: HttpServletRequest,
        @PathVariable documentId: String
    ): ResponseEntity<*> {
        return super.responseValue(request, apiWorkflowService.getDocumentDataStructure(documentId))
    }

    @GetMapping("/component/{componentId}")
    fun getComponent(
        request: HttpServletRequest,
        @PathVariable componentId: String
    ): ResponseEntity<*> {
        return super.responseValue(request, apiWorkflowService.getComponent(componentId))
    }
}
