/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.resource.controller

import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.mapper.AliceFileMapper
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.itsm.resource.dto.ResourceSearchDto
import co.brainz.itsm.resource.service.ResourceService
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/resources")
class ResourceController(
    private val resourceService: ResourceService,
    private val aliceFileProvider: AliceFileProvider
) {
    private val fileMapper: AliceFileMapper = Mappers.getMapper(AliceFileMapper::class.java)
    private val resourcePage: String = "resource/resources"
    private val resourcePageFragment: String = "resource/resources :: list"
    
    /**
     * 리소스 관리 페이지 호출.
     */
    @GetMapping("")
    fun getResourcePage(searchCondition: ResourceSearchDto, model: Model): String {
        println(searchCondition)

        return if (searchCondition.isPaging) {
            val resources = resourceService.getResources(searchCondition)
            model.addAttribute("pageType", searchCondition.pageType)
            model.addAttribute("resources", resources.data)
            model.addAttribute("paging", resources.paging)
            resourcePageFragment
        } else {
            // 파일 확장자 목록
            val fileNameExtensions = mutableListOf<AliceFileNameExtensionDto>()
            val foundFileNameExtensions = aliceFileProvider.getFileNameExtension()
            for (foundFileNameExtension in foundFileNameExtensions) {
                fileNameExtensions.add(fileMapper.toAliceFileNameExtensionDto(foundFileNameExtension))
            }
            model.addAttribute("acceptFileNameList", fileNameExtensions)

            resourcePage
        }
    }
}
