package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController(private val documentService: DocumentService) {

    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/data/{documentId}")
    fun getDocument(@PathVariable documentId: String): String {
        return documentService.findDocument(documentId)
    }
}