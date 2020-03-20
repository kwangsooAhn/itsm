package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(private val documentService: DocumentService,
                         private val formService: FormService,
                         private val progressService: ProcessService) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
    private val documentCreatePage: String = "document/documentNew"
    private val documentEditPage: String = "document/documentEdit"

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
     * 신청서 생성 화면.
     *
     * @param documentId
     * @return model
     */
    @GetMapping("/new")
    fun getDocumentNew(model: Model): String {
        var formParams = LinkedMultiValueMap<String, String>()
        var processParams = LinkedMultiValueMap<String, String>()
        formParams["status"] = RestTemplateConstants.FormStatus.PUBLISH.value
        processParams["status"] = RestTemplateConstants.ProcessStatus.PUBLISH.value
        model.addAttribute("formList", formService.findForms(formParams))
        model.addAttribute("processList", progressService.getProcesses(processParams))
        return documentCreatePage
    }

    /**
     * 신청서 작성 화면.
     *
     * @param documentId
     * @return String
     */
    @GetMapping("{documentId}/edit")
    fun getDocumentEdit(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        return documentEditPage
    }
}