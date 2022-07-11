/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.document.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import co.brainz.itsm.role.service.RoleService
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
    private val roleService: RoleService,
    private val numberingRuleService: NumberingRuleService
) {

    private val workFlowSearchPage: String = "workflow/workFlowSearch"
    private val workFlowListPage: String = "workflow/workFlowList"
    private val workFlowEditPage: String = "workflow/workFlow"
    private val documentDisplayModalPage: String = "workflow/documentDisplayModal"
    private val workFlowImportModalPage: String = "workflow/workFlowImportModal"

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
     * @param documentSearchCondition
     * @param model
     * @return String
     */
    @GetMapping("")
    fun getWorkFlowList(documentSearchCondition: DocumentSearchCondition, model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_TYPE_P_CODE))
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        val result = documentService.getDocumentList(documentSearchCondition)
        model.addAttribute("documentList", result.data)
        model.addAttribute("paging", result.paging)
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
        model.addAttribute("view", false)
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingRuleService.getNumberingRules())
        model.addAttribute("typeList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_TYPE_P_CODE))
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        model.addAttribute("allRoles", roleService.getAllRoleList())

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
        val documentData = documentService.getDocumentAdmin(documentId)
        model.addAttribute("view", false)
        model.addAttribute("documentId", documentId)
        model.addAttribute("documentData", documentData)
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("formList", documentService.getFormList())
        model.addAttribute("processList", documentService.getProcessList())
        model.addAttribute("numberingRuleList", numberingRuleService.getNumberingRules())
        model.addAttribute("typeList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_TYPE_P_CODE))
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        model.addAttribute("allRoles", roleService.getAllRoleList())
        model.addAttribute("documentRoles", roleService.getDocumentRoleList(documentData))

        return workFlowEditPage
    }

    /**
     * 업무흐름 링크 수정 화면.
     *
     * @param documentId
     * @return String
     */
    @GetMapping("/workflowLink/{documentId}/edit")
    fun getWorkFlowLinkEdit(@PathVariable documentId: String, model: Model): String {
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("documentData", documentService.getDocumentLinkAdmin(documentId))
        model.addAttribute("typeList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_TYPE_P_CODE))
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        model.addAttribute("allRoles", roleService.getAllRoleList())
        model.addAttribute("documentRoles", roleService.getDocumentLinkRoleList(documentId))
        return workFlowEditPage
    }

    /**
     * 신청서 양식 편집 화면.
     *
     * @param documentId
     */
    @GetMapping("/{documentId}/display")
    fun getWorkFlowDisplay(@PathVariable documentId: String, model: Model): String {
        model.addAttribute(
            "documentDisplayTypeList",
            codeService.selectCodeByParent(DocumentConstants.DOCUMENT_DISPLAY_TYPE_P_CODE)
        )
        model.addAttribute("documentDisplayList", documentService.getDocumentDisplay(documentId))
        return documentDisplayModalPage
    }

    /**
     * 업무흐름 Import 모달
     */
    @GetMapping("/import")
    fun getWorkFlowImport(model: Model): String {
        model.addAttribute("statusList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_STATUS_P_CODE))
        model.addAttribute("numberingRuleList", numberingRuleService.getNumberingRules())
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        return workFlowImportModalPage
    }
}
