/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.documentStorage.service.DocumentStorageService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documentStorage")
class DocumentStorageRestController(private val documentStorageService: DocumentStorageService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // Document Storage insert
    @PostMapping("/{instanceId}")
    fun insertDocumentStorage(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentStorageService.insertDocumentStorage(instanceId))
    }

    // Document Storage delete
    @DeleteMapping("/{instanceId}")
    fun deleteDocumentStorage(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(documentStorageService.deleteDocumentStorage(instanceId))
    }

    // Document Storagte 데이터 존재 여부 확인
    @GetMapping("/{instanceId}/exist")
    fun getDocumentStorageDataExist(@PathVariable instanceId: String): Boolean {
        return documentStorageService.getDocumentStorageDataExist(instanceId)
    }
}
