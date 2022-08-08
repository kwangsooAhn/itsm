/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.controller

import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.dto.AliceResourceSearchDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.itsm.cmdb.ciIcon.service.CIIconService
import java.io.File
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/icons")
class CIIconController(
    private val aliceResourceProvider: AliceResourceProvider,
    private val ciIconService: CIIconService
) {
    private val iconPage: String = "cmdb/icon/icons"
    private val iconPageFragment: String = "cmdb/icon/icons :: list"

    @GetMapping("")
    fun getIconPage(searchCondition: AliceResourceSearchDto, model: Model): String {
        // 기본 경로
        val type = ResourceConstants.FileType.CI_ICON.code
        val basePath = aliceResourceProvider.getExternalPath(type).toAbsolutePath().toString()
        return if (searchCondition.isPaging) {
            val resources = ciIconService.getCIIconsPaging(searchCondition)
            // 검색어 사용여부
            model.addAttribute("isSearch", searchCondition.searchValue.isNotEmpty())
            // 페이지 타입
            model.addAttribute("pageType", searchCondition.pageType)
            // 데이터
            model.addAttribute("resources", resources.data)
            model.addAttribute("paging", resources.paging)
            // 최 상위일 경우
            model.addAttribute("isRoot", basePath == searchCondition.searchPath)
            iconPageFragment
        } else {
            // 구분자
            model.addAttribute("fileSeparator", File.separator)
            // 기본 경로
            model.addAttribute("basePath", basePath)
            // 허용 확장자 목록 (이미지 파일만 허용)
            model.addAttribute("acceptFileExtensions", aliceResourceProvider.getAllowedExtensions(type))
            iconPage
        }
    }
}
