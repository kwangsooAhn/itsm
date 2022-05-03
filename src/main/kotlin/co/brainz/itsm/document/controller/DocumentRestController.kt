/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.service.DocumentActionService
import co.brainz.itsm.document.service.DocumentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController(
    private val documentService: DocumentService,
    private val documentActionService: DocumentActionService
) {

    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/{documentId}/data")
    fun getDocumentData(@PathVariable documentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            documentActionService.makeDocumentAction(documentService.getDocumentData(documentId))
        )
    }

    /**
     * 신청서 목록 조회.
     */
    @GetMapping("/", "")
    fun getDocuments(
        documentSearchCondition: DocumentSearchCondition
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ZResponse(
                data = documentService.getDocumentList(documentSearchCondition).data
            )
        )
    }

    @GetMapping("/components/{componentId}/value")
    fun getDocumentComponentValue(
        @PathVariable componentId: String,
        @RequestParam(value = "documentNo", defaultValue = "") documentNo: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            documentService.getDocumentComponentValue(documentNo, componentId)
        )
    }
}
