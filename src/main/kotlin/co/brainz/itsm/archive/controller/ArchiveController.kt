/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.controller

import co.brainz.itsm.archive.constants.ArchiveConstants
import co.brainz.itsm.archive.dto.ArchiveSearchCondition
import co.brainz.itsm.archive.service.ArchiveService
import co.brainz.itsm.code.service.CodeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/archives")
class ArchiveController(
    private val codeService: CodeService,
    private val archiveService: ArchiveService
) {

    private val archiveSearchPage: String = "archive/archiveSearch"
    private val archiveListPage: String = "archive/archiveList"
    private val archiveEditPage: String = "archive/archiveEdit"
    private val archiveViewPage: String = "archive/archiveView"

    /**
     * [model]를 받아서 자료실 호출 화면.
     *
     */
    @GetMapping("/search")
    fun getArchiveSearch(model: Model): String {
        model.addAttribute(
            "categoryList",
            codeService.selectCodeByParent(ArchiveConstants.ARCHIVE_CATEGORY_P_CODE)
        )
        return archiveSearchPage
    }

    /**
     * [archiveSearchCondition], [model]를 받아서 자료실 리스트 화면 호출.
     *
     */
    @GetMapping("")
    fun getArchiveList(archiveSearchCondition: ArchiveSearchCondition, model: Model): String {
        val result = archiveService.getArchiveList(archiveSearchCondition)
        model.addAttribute("archiveList", result.data)
        model.addAttribute("paging", result.paging)
        return archiveListPage
    }

    /**
     * [model]를 받아서 자료실 신규 등록 화면 호출.
     *
     */
    @GetMapping("/new")
    fun getArchiveNew(model: Model): String {
        model.addAttribute(
            "categoryList",
            codeService.selectCodeByParent(ArchiveConstants.ARCHIVE_CATEGORY_P_CODE)
        )
        return archiveEditPage
    }

    /**
     * 자료실 상세 조회 화면.
     *
     * @param archiveId
     * @param model
     * @return String
     */
    @GetMapping("/{archiveId}/view")
    fun getArchiveView(@PathVariable archiveId: String, model: Model): String {
        model.addAttribute("archive", archiveService.getArchiveDetail(archiveId, "view"))
        return archiveViewPage
    }

    /**
     * 자료실 편집 화면.
     *
     * @param archiveId
     * @param model
     * @return String
     */
    @GetMapping("/{archiveId}/edit")
    fun getArchiveEdit(@PathVariable archiveId: String, model: Model): String {
        model.addAttribute("archive", archiveService.getArchiveDetail(archiveId, "edit"))
        model.addAttribute(
            "categoryList",
            codeService.selectCodeByParent(ArchiveConstants.ARCHIVE_CATEGORY_P_CODE)
        )
        return archiveEditPage
    }
}
