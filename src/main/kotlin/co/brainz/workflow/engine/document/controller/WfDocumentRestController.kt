package co.brainz.workflow.engine.document.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import org.springframework.web.bind.annotation.*
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
     * @return FormComponentViewDto
     */
    @GetMapping("{documentId}")
    fun getDocument(@PathVariable documentId: String): WfFormComponentViewDto? {
        return wfEngine.document().document(documentId)
    }


    /**
     * 신청서 등록.
     *
     * @return DocumentDto
     */
    @PostMapping("")
    fun createDocument(@RequestBody documentDto: WfDocumentDto): WfDocumentDto {
        return wfEngine.document().createDocument(documentDto)
    }

    /**
     * 신청서 삭제.
     */
    @Transactional
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String) {
        return wfEngine.document().deleteDocument(documentId)
    }
}
