/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.document.controller

import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.service.DocumentService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/documents")
class DocumentController(
    private val documentService: DocumentService
) {

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
    @GetMapping("")
    fun getDocumentList(documentSearchCondition: DocumentSearchCondition, model: Model): String {
        val result = documentService.getDocumentList(documentSearchCondition)
        model.addAttribute("documentList", result.data)
        model.addAttribute("paging", result.paging)
        return documentListPage
    }
}
