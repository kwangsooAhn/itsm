/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.document.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/workflows")
class WorkFlowController(
    private val documentService: DocumentService,
    private val codeService: CodeService,
    private val numberingRuleService: NumberingRuleService
) {

    private val workFlowSearchPage: String = "workflow/workFlowSearch"
    private val workFlowListPage: String = "workflow/workFlowList"
    private val workFlowListFragment: String = "workflow/workFlowList :: list"
    private val workFlowEditModalPage: String = "workflow/workFlowEditModal"
    private val documentDisplayModalPage: String = "workflow/documentDisplayModal"

    /**
     * 업무흐름 리스트 호출 화면.
     *
     * @return String
     */
    @GetMapping("/search")
    fun getWorkFlowMngSearch(model: Model): String {
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        return workFlowSearchPage
    }

    /**
     * 업무흐름 리스트 화면.
     *
     * @param restTemplateDocumentSearchListDto
     * @param model
     * @return String
     */
    @GetMapping("")
    fun getWorkFlowList(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto, model: Model): String {
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        val result = documentService.getDocumentList(restTemplateDocumentSearchListDto)
        model.addAttribute("documentList", result.data)
        model.addAttribute("documentCount", result.totalCount)
        return if (restTemplateDocumentSearchListDto.isScroll) workFlowListFragment else workFlowListPage
    }

    /**
     * 업무흐름 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getWorkFlowNew(model: Model): String {
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingRuleService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return workFlowEditModalPage
    }

    /**
     * 업무흐름 수정 화면.
     *
     * @param documentId
     * @param model
     * @return String
     */
    @GetMapping("{documentId}/edit")
    fun getWorkFlowEdit(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentId", documentId)
        model.addAttribute("documentData", documentService.getDocumentAdmin(documentId))
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingRuleService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return workFlowEditModalPage
    }

    /**
     * 신청서 양식 편집 화면.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}/display")
    fun getWorkFlowDisplay(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("documentDisplayTypeList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_DISPLAY_TYPE_P_CODE))
        model.addAttribute("documentDisplayList", documentService.getDocumentDisplay(documentId))
        return documentDisplayModalPage
    }
}
