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
    
    /**
     * 리소스 관리 페이지 호출.
     */
    @GetMapping("")
    fun getResourcePage(searchCondition: AliceResourceSearchDto, model: Model): String {
        return if (searchCondition.isPaging) {
            val resources = aliceResourceProvider.getResourcesPaging(searchCondition)
            // 검색어 사용여부
            model.addAttribute("isSearch", searchCondition.searchValue.isNotEmpty())
            // 페이지 타입
            model.addAttribute("pageType", searchCondition.pageType)
            model.addAttribute("resources", resources.data)
            model.addAttribute("paging", resources.paging)
            resourcePageFragment
        } else {
            // 구분자
            model.addAttribute("fileSeparator", File.separator)
            // 기본 경로
            model.addAttribute("basePath", aliceResourceProvider.getExternalPath(searchCondition.type)
                .toAbsolutePath().toString())
            // 허용 확장자 목록
            model.addAttribute("acceptFileExtensions", aliceResourceProvider.getAllowedExtensions(searchCondition.type))
            resourcePage
        }
    }
}
