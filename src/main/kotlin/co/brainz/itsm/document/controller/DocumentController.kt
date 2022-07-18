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
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(
    private val documentService: DocumentService,
    private val codeService: CodeService
) {

    private val documentSearchPage: String = "document/documentSearch"
    private val documentListPage: String = "document/documentList"
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
    @GetMapping("")
    fun getDocumentList(documentSearchCondition: DocumentSearchCondition, model: Model): String {
        val result = documentService.getDocumentList(documentSearchCondition)
        model.addAttribute("groupList", codeService.selectCodeByParent(DocumentConstants.DOCUMENT_GROUP_P_CODE))
        model.addAttribute("documentList", result.data)
        model.addAttribute("paging", result.paging)
        return documentListPage
    }

    /**
     * 신청서 상세 화면.
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
