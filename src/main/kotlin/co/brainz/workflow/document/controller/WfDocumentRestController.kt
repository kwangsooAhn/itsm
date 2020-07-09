package co.brainz.workflow.document.controller

import co.brainz.workflow.document.service.WfDocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import javax.transaction.Transactional
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
@RequestMapping("/rest/wf/documents")
class WfDocumentRestController(
    private val wfDocumentService: WfDocumentService
) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<RestTemplateDocumentDto>
     */
    @GetMapping("")
    fun getDocuments(
        restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto
    ): List<RestTemplateDocumentDto> {
        return wfDocumentService.documents(restTemplateDocumentSearchListDto)
    }

    /**
     * 신청서 1건 조회.
     *
     * @param documentId
     * @return RestTemplateDocumentDto
     */
    @GetMapping("/{documentId}")
    fun getDocument(@PathVariable documentId: String): RestTemplateDocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * 신청서 데이터 조회.
     *
     * @param documentId
     * @return RestTemplateRequestDocumentDto
     */
    @GetMapping("/{documentId}/data")
    fun getDocumentData(@PathVariable documentId: String): RestTemplateRequestDocumentDto {
        return wfDocumentService.getInitDocument(documentId)
    }

    /**
     * 신청서 등록.
     *
     * @param restTemplateDocumentDto
     * @return RestTemplateDocumentDto
     */
    @PostMapping("")
    fun createDocument(@RequestBody restTemplateDocumentDto: RestTemplateDocumentDto): RestTemplateDocumentDto {
        return wfDocumentService.createDocument(restTemplateDocumentDto)
    }

    /**
     * 신청서 수정.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
    @Transactional
    @PutMapping("/{documentId}")
    fun updateDocument(
        @PathVariable documentId: String,
        @RequestBody restTemplateDocumentDto: RestTemplateDocumentDto,
        @RequestParam params: LinkedHashMap<String, Any>
    ): Boolean {
        return wfDocumentService.updateDocument(restTemplateDocumentDto, params)
    }

    /**
     * 신청서 삭제
     *
     * @param documentId
     * @return Boolean
     */
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String): Boolean {
        return wfDocumentService.deleteDocument(documentId)
    }

    /**
     * 신청서 양식 정보 조회.
     *
     * @param documentId
     * @return RestTemplateDocumentDisplayViewDto
     */
    @GetMapping("/{documentId}/display")
    fun getDocumentDisplay(@PathVariable documentId: String): RestTemplateDocumentDisplayViewDto {
        return wfDocumentService.getDocumentDisplay(documentId)
    }

    /**
     * 신청서 양식 정보 수정.
     *
     * @param restTemplateDocumentDisplaySaveDto
     * @return Boolean
     */
    @PutMapping("/{documentId}/display")
    fun updateDocumentDisplay(
        @RequestBody restTemplateDocumentDisplaySaveDto: RestTemplateDocumentDisplaySaveDto,
        @PathVariable documentId: String
    ): Boolean {
        return wfDocumentService.updateDocumentDisplay(restTemplateDocumentDisplaySaveDto)
    }
}
