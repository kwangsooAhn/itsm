/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.document.dto.DocumentEditDto
import co.brainz.itsm.document.dto.DocumentImportDto
import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.ResponseEntity
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
     * @param documentEditDto
     * */
    @PostMapping("")
    fun workFlowDocument(@RequestBody documentEditDto: DocumentEditDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.createDocument(documentEditDto))
    }

    /**
     * 업무흐름링크 등록.
     *
     * @param documentEditDto
     * */
    @PostMapping("/workflowLink")
    fun workFlowLink(@RequestBody documentEditDto: DocumentEditDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.createDocumentLink(documentEditDto))
    }

    /**
     * 업무흐름 삭제
     *
     * @param documentId
     * */
    @DeleteMapping("/{documentId}")
    fun deleteWorkFlow(@PathVariable documentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.deleteDocument(documentId))
    }

    /**
     * 업무흐름링크 삭제
     *
     * @param documentId
     * */
    @DeleteMapping("/workflowLink/{documentId}")
    fun deleteWorkFlowLink(@PathVariable documentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.deleteDocumentLink(documentId))
    }

    /**
     * 업무흐름 조회.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}")
    fun getWorkFlow(@PathVariable documentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.getDocument(documentId))
    }

    /**
     * 신청서 수정
     *
     * @param documentId
     * @param documentDto
     * */
    @PutMapping("/{documentId}")
    fun updateWorkFlow(
        @PathVariable documentId: String,
        @RequestBody documentEditDto: DocumentEditDto,
        @RequestParam(value = "isDeleteData", defaultValue = "false") isDeleteData: String
    ): ResponseEntity<ZResponse> {
        val params = LinkedHashMap<String, Any>()
        params["isDeleteData"] = isDeleteData
        return ZAliceResponse.response(documentService.updateDocument(documentEditDto, params))
    }

    /**
     * 신청서링크 수정
     *
     * @param documentId
     * @param String
     * */
    @PutMapping("/workflowLink/{documentId}")
    fun updateWorkFlowLink(
        @PathVariable documentId: String,
        @RequestBody documentEditDto: DocumentEditDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.updateDocumentLink(documentEditDto))
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
        @RequestBody documentDisplay: RestTemplateDocumentDisplaySaveDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.updateDocumentDisplay(documentDisplay))
    }

    /**
     * 업무흐름 Export 데이터 조회.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}/export")
    fun getExportWorkFlowData(@PathVariable documentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentService.getDocumentExportData(documentId))
    }

    /**
     * 업무흐름 Import.
     */
    @PostMapping("/import")
    fun importWorkFlowData(@RequestBody jsonData: Any): ResponseEntity<ZResponse> {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return ZAliceResponse.response(
            documentService.importDocumentData(mapper.convertValue(jsonData, DocumentImportDto::class.java))
        )
    }
}
