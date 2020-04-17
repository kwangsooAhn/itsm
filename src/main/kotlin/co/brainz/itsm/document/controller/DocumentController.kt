package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.form.service.FormService
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
class DocumentController(
    private val documentService: DocumentService,
    private val formService: FormService,
    private val processService: ProcessService
) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
    private val documentCreatePage: String = "document/documentNew"
    private val documentEditPage: String = "document/documentEdit"
    private val documentDisplayPage: String = "document/documentDisplay"

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
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getDocumentNew(model: Model): String {
        val formParams = LinkedMultiValueMap<String, String>()
        val formStatus = ArrayList<String>()
        formStatus.add(RestTemplateConstants.FormStatus.PUBLISH.value)
        formStatus.add(RestTemplateConstants.FormStatus.USE.value)
        formParams["status"] = formStatus.joinToString(",")

        val processParams = LinkedMultiValueMap<String, String>()
        val processStatus = ArrayList<String>()
        processStatus.add(RestTemplateConstants.ProcessStatus.PUBLISH.value)
        processStatus.add(RestTemplateConstants.ProcessStatus.USE.value)
        processParams["status"] = processStatus.joinToString(",")

        model.addAttribute("formList", formService.findForms(formParams))
        model.addAttribute("processList", processService.getProcesses(processParams))
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

    /**
     * 신청서 양식 편집 화면
     *
     * @return string
     * */
    @GetMapping("/display/{documentId}")
    fun getDocumentDisplay(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
//        model.addAttribute("documentDisplayList",  documentService.findDocumentDisplay(documentId))
        return documentDisplayPage
    }
}
