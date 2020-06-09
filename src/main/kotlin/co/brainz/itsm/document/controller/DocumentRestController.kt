package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController(
    private val documentService: DocumentService
) {

    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/{documentId}/data")
    fun getDocumentData(@PathVariable documentId: String): String {
        return documentService.getDocumentData(documentId)
    }

    /**
     * 신청서 목록 조회.
     */
    @GetMapping("/", "")
    fun getDocuments(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
        return documentService.getDocumentList(restTemplateDocumentSearchListDto)
    }
}
