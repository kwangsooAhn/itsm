package co.brainz.workflow.document.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.document.dto.WfDocumentDto
import co.brainz.workflow.form.dto.WfFormComponentViewDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
