package co.brainz.workflow.document.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.document.dto.DocumentDto
import co.brainz.workflow.form.dto.FormComponentViewDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/documents")
class WFDocumentRestController(private val wfEngine: WFEngine) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    @GetMapping("")
    fun getDocuments(): List<DocumentDto> {
        return wfEngine.document().documents()
    }

    /**
     * 신청서 1건 조회.
     *
     * @param documentId
     * @return FormComponentViewDto
     */
    @GetMapping("{documentId}")
    fun getDocument(@PathVariable documentId: String): FormComponentViewDto? {
        return wfEngine.document().document(documentId)
    }
}
