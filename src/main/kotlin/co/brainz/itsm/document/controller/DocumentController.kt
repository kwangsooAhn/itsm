package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(private val documentService: DocumentService) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"

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
    fun getDocumentList(model: Model): String {
        model.addAttribute("documentList", documentService.findDocumentList())
        return documentListPage
    }

    /**
     * 신청서 작성 화면.
     *
     * @param documentId
     */
    @GetMapping("{documentId}/edit")
    fun getDocumentEdit(@PathVariable documentId: String) {
        //TODO return 신청서 작성 화면.
    }
}
