package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(
    private val documentService: DocumentService
) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
    private val documentPrintPage: String = "document/documentPrint"

    /**
     * 신청서 리스트 호출 화면.
     *
     * @return String
     */
    @GetMapping("/search")
    fun getDocumentSearch(): String {
        return documentSearchPage
    }

    /**
     * 신청서 리스트 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getDocumentList(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto, model: Model): String {
        val result = documentService.getDocumentList(restTemplateDocumentSearchListDto)
        model.addAttribute("documentList", result)
        model.addAttribute("totalCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return documentListPage
    }

    /**
     * 신청서 인쇄 화면.
     */
    @GetMapping("/{documentId}/print")
    fun getDocumentPrint(@PathVariable documentId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("time", ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")))
        return documentPrintPage
    }
}
