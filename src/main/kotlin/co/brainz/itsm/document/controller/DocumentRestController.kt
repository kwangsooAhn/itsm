package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    /**
     * 신청서 생성
     *
     * @param restTemplateDocumentDto
     * */
    @PostMapping("")
    fun createDocument(@RequestBody restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        return documentService.createDocument(restTemplateDocumentDto)
    }

    /**
     * 신청서 삭제
     *
     * @param documentId
     * */
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String): ResponseEntity<String> {
        return documentService.deleteDocument(documentId)
    }

    /**
     * 신청서 목록 조회.
     */
    @GetMapping("/", "")
    fun getDocuments(): List<RestTemplateDocumentDto> {
        return documentService.findDocumentList()
    }

    /**
     * 신청서 편집 데이터 조회.
     */
    @GetMapping("/display/{documentId}")
    fun getDocumentDisplay(@PathVariable documentId: String): String {
        return documentService.findDocumentDisplay(documentId)
    }

}
