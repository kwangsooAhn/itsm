/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
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
@RequestMapping("/rest/workflows")
class WorkFlowRestController(
    private val documentService: DocumentService
) {

    /**
     * 업무흐름 등록.
     *
     * @param restTemplateDocumentDto
     * */
    @PostMapping("")
    fun workFlowDocument(@RequestBody restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        return documentService.createDocument(restTemplateDocumentDto)
    }

    /**
     * 업무흐름 삭제
     *
     * @param documentId
     * */
    @DeleteMapping("/{documentId}")
    fun deleteWorkFlow(@PathVariable documentId: String): ResponseEntity<String> {
        return documentService.deleteDocument(documentId)
    }

    /**
     * 업무흐름 조회.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}")
    fun getWorkFlow(@PathVariable documentId: String): String {
        return documentService.getDocument(documentId)
    }

    /**
     * 신청서 수정
     *
     * @param documentId
     * @param restTemplateDocumentDto
     * */
    @PutMapping("/{documentId}")
    fun updateWorkFlow(
        @PathVariable documentId: String,
        @RequestBody restTemplateDocumentDto: RestTemplateDocumentDto,
        @RequestParam(value = "isDeleteData", defaultValue = "false") isDeleteData: String
    ): String? {
        val params = LinkedMultiValueMap<String, String>()
        params["isDeleteData"] = isDeleteData
        return documentService.updateDocument(restTemplateDocumentDto, params)
    }

    /**
     * 신청서 편집 데이터 조회.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}/display")
    fun getWorkFlowDisplay(@PathVariable documentId: String): String {
        return documentService.getDocumentDisplay(documentId)
    }

    /**
     * 신청서 편집 데이터 저장.
     *
     * @param documentId
     * @param documentDisplay
     */
    @PutMapping("/{documentId}/display")
    fun updateWorkFlowDisplay(
        @PathVariable documentId: String,
        @RequestBody documentDisplay: RestTemplateDocumentDisplayDto
    ): Boolean {
        return documentService.updateDocumentDisplay(documentDisplay)
    }
}
