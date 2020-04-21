package co.brainz.workflow.engine.document.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/documents")
class WfDocumentRestController(private val wfEngine: WfEngine) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    @GetMapping("")
    fun getDocuments(): List<WfDocumentDto> {
        return wfEngine.document().documents()
    }

    /**
     * 신청서 1건 조회.
     *
     * @param documentId
     * @return WfDocumentDto
     */
    @GetMapping("/{documentId}")
    fun getDocument(@PathVariable documentId: String): WfDocumentDto {
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
     * @param documentDto
     * @return WfDocumentDto
     */
    @PostMapping("")
    fun createDocument(@RequestBody documentDto: WfDocumentDto): WfDocumentDto {
        return wfEngine.document().createDocument(documentDto)
    }

    @Transactional
    @PutMapping("/{documentId}")
    fun updateDocument(@PathVariable documentId: String, @RequestBody documentDto: WfDocumentDto): Boolean {
        return wfEngine.document().updateDocument(documentDto)
    }

    /**
     * 신청서 삭제
     */
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String): Boolean {
        return wfEngine.document().deleteDocument(documentId)
    }
}
