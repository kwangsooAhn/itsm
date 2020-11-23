/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

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
@RequestMapping("/workflows")
class WorkFlowController(
    private val documentService: DocumentService,
    private val codeService: CodeService,
    private val numberingService: AliceNumberingService
) {

    private val workFlowSearchPage: String = "workFlow/workFlowSearch"
    private val workFlowListPage: String = "workFlow/workFlowList"
    private val workFlowEditPage: String = "workFlow/workFlowEdit"

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
        model.addAttribute("documentList", result)
        model.addAttribute("documentCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return workFlowListPage
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
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return workFlowEditPage
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
        model.addAttribute("numberingRuleList", numberingService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))

        return workFlowEditPage
    }
}