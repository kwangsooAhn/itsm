package co.brainz.workflow.engine.document.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import javax.transaction.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/documents")
class WfDocumentRestController(private val wfEngine: WfEngine) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<RestTemplateDocumentDto>
     */
    @GetMapping("")
    fun getDocuments(): List<RestTemplateDocumentDto> {
        return wfEngine.document().documents()
    }

    /**
     * 신청서 1건 조회.
     *
     * @param documentId
     * @return RestTemplateDocumentDto
     */
    @GetMapping("/{documentId}")
    fun getDocument(@PathVariable documentId: String): RestTemplateDocumentDto {
        return wfEngine.document().getDocument(documentId)
    }

    /**
     * 신청서 데이터 조회.
     *
     * @param documentId
     * @return FormComponentViewDto
     */
    @GetMapping("/{documentId}/data")
    fun getDocumentData(@PathVariable documentId: String): WfFormComponentViewDto? {
        return wfEngine.document().getDocumentData(documentId)
    }

    /**
     * 신청서 등록.
     *
     * @param restTemplateDocumentDto
     * @return RestTemplateDocumentDto
     */
    @PostMapping("")
    fun createDocument(@RequestBody restTemplateDocumentDto: RestTemplateDocumentDto): RestTemplateDocumentDto {
        return wfEngine.document().createDocument(restTemplateDocumentDto)
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
        @RequestBody restTemplateDocumentDto: RestTemplateDocumentDto
    ): Boolean {
        return wfEngine.document().updateDocument(restTemplateDocumentDto)
    }

    /**
     * 신청서 삭제
     *
     * @param documentId
     * @return Boolean
     */
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String): Boolean {
        return wfEngine.document().deleteDocument(documentId)
    }

    /**
     * 신청서 양식 정보 조회.
     *
     * @param documentId
     * @return RestTemplateDocumentDisplayViewDto
     */
    @GetMapping("/{documentId}/display")
    fun getDocumentDisplay(@PathVariable documentId: String): RestTemplateDocumentDisplayViewDto {
        return wfEngine.document().getDocumentDisplay(documentId)
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
        return wfEngine.document().updateDocumentDisplay(restTemplateDocumentDisplaySaveDto)
    }
}
