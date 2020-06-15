package co.brainz.itsm.document.controller

import co.brainz.framework.numbering.service.AliceNumberingService
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents-admin")
class DocumentAdminController(
    private val documentService: DocumentService,
    private val codeService: CodeService,
    private val numberingService: AliceNumberingService
) {

    private val documentAdminSearchPage: String = "document/documentAdminSearch"
    private val documentAdminListPage: String = "document/documentAdminList"
    private val documentEditPage: String = "document/documentEdit"
    private val documentDisplayPage: String = "document/documentDisplay"

    /**
     * 업무흐름 리스트 호출 화면.
     *
     * @return String
     */
    @GetMapping("/search")
    fun getDocumentMngSearch(model: Model): String {
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        return documentAdminSearchPage
    }

    /**
     * 업무흐름 리스트 화면.
     *
     * @param restTemplateDocumentSearchListDto
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getDocumentList(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto, model: Model): String {
        model.addAttribute("documentList", documentService.getDocumentList(restTemplateDocumentSearchListDto))
        return documentAdminListPage
    }

    /**
     * 업무흐름 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getDocumentNew(model: Model): String {
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return documentEditPage
    }

    /**
     * 업무흐름 수정 화면.
     *
     * @param documentId
     * @param model
     * @return String
     */
    @GetMapping("{documentId}/edit")
    fun getDocumentEdit(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return documentEditPage
    }

    /**
     * 업무흐름 양식 편집 화면
     *
     * @param documentId
     * @param model
     * @return String
     * */
    @GetMapping("{documentId}/display")
    fun getDocumentDisplay(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        return documentDisplayPage
    }
}
