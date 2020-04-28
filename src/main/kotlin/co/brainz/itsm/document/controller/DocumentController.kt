package co.brainz.itsm.document.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.customCode.service.CustomCodeService
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.service.DocumentService
import co.brainz.framework.numbering.service.AliceNumberingService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/documents")
class DocumentController(
        private val documentService: DocumentService,
        private val codeService: CodeService,
        private val customCodeService: CustomCodeService,
        private val numberingService: AliceNumberingService
) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
    private val documentPublishPage: String = "document/documentPublish"
    private val documentEditPage: String = "document/documentEdit"
    private val documentDisplayPage: String = "document/documentDisplay"

    private val documentCustomCodePage: String = "document/customCodeData"

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
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())

        return documentEditPage
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

    @GetMapping("{documentId}/edit")
    fun getDocumentEdit(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())

        return documentEditPage
    }

    /**
     * 신청서 양식 편집 화면
     *
     * @return string
     * */
    @GetMapping("{documentId}/display")
    fun getDocumentDisplay(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        return documentDisplayPage
    }


    /**
     * 사용자 정의 코드 데이터 조회.
     */
    @RequestMapping("/custom-code/{customCodeId}/data", method = [RequestMethod.POST, RequestMethod.GET])
    fun getCustomCodeData(@PathVariable customCodeId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("customCodeData", request.getParameter("customCodeData")?:"")
        model.addAttribute("customCodeDataList", customCodeService.getCustomCodeData(customCodeId))
        return documentCustomCodePage
    }
}
