package co.brainz.itsm.document.controller

import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(
    private val documentService: DocumentService
) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
    private val documentPublishPage: String = "document/documentPublish"
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
        restTemplateDocumentSearchListDto.searchDocumentType = DocumentConstants.DocumentType.APPLICATION_FORM.value
        model.addAttribute("documentList", documentService.getDocumentList(restTemplateDocumentSearchListDto))
        return documentListPage
    }

    /**
     * 신청서 작성 화면.
     *
     * @param documentId
     * @return String
     */
    @GetMapping("{documentId}/publish")
    fun getDocumentWrite(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        return documentPublishPage
    }

    /**
     * 신청서 인쇄 화면.
     */
    @PostMapping("/{documentId}/print")
    fun getDocumentPrint(@PathVariable documentId: String, model: Model, request: HttpServletRequest): String {
        val gmt = AliceTimezoneUtils().toGMT(LocalDateTime.now().withNano(0))
        model.addAttribute("time", AliceTimezoneUtils().toZonedDateTime(gmt))
        model.addAttribute("data", request.getParameter("data") ?: "")
        return documentPrintPage
    }
}
