/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.resource

import co.brainz.framework.resourceManager.dto.AliceResourceSearchDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import java.io.File
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/resources")
class ResourceController(
    private val aliceResourceProvider: AliceResourceProvider
) {
    private val resourcePage: String = "resource/resources"
    private val resourcePageFragment: String = "resource/resources :: list"
    private val thumbnailModal: String = "resource/thumbnailModal"
    private val thumbnailModalFragment: String = "resource/thumbnailModal :: list"

    /**
     * 리소스 관리 페이지 호출.
     */
    @GetMapping("")
    fun getResourcePage(searchCondition: AliceResourceSearchDto, model: Model): String {
        // 기본 경로
        val basePath = aliceResourceProvider.getExternalPath(searchCondition.type).toAbsolutePath().toString()
        return if (searchCondition.isPaging) {
            val resources = aliceResourceProvider.getResourcesPaging(searchCondition)
            // 검색어 사용여부
            model.addAttribute("isSearch", searchCondition.searchValue.isNotEmpty())
            // 페이지 타입
            model.addAttribute("pageType", searchCondition.pageType)
            // 데이터
            model.addAttribute("resources", resources.data)
            model.addAttribute("paging", resources.paging)
            // 최 상위일 경우
            model.addAttribute("isRoot", basePath == searchCondition.searchPath)
            resourcePageFragment
        } else {
            // 구분자
            model.addAttribute("fileSeparator", File.separator)
            // 기본 경로
            model.addAttribute("basePath", basePath)
            // 허용 확장자 목록
            model.addAttribute("acceptFileExtensions", aliceResourceProvider.getAllowedExtensions(searchCondition.type))
            resourcePage
        }
    }

    /**
     * 썸네일 모달 페이지 호출.
     */
    @GetMapping("/thumbnail")
    fun getThumbnailModal(searchCondition: AliceResourceSearchDto, model: Model): String {
        val resources = aliceResourceProvider.getResourcesScroll(searchCondition)
        // 타입
        model.addAttribute("type", searchCondition.type)
        // 데이터
        model.addAttribute("resources", resources.data)
        model.addAttribute("scroll", resources.scroll)
        // 썸네일 크기를 작게할지 여부
        model.addAttribute("small", aliceResourceProvider.isThumbnailSmall(searchCondition.type))

        return if (searchCondition.isPaging) {
            thumbnailModalFragment
        } else {
            thumbnailModal
        }
    }
}
