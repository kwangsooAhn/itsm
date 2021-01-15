/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.controller

import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.itsm.cmdb.ciType.service.CITypeService
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/types")
class CITypeRestController(private val ciTypeService: CITypeService) {

    /**
     * CI Type 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getCmdbTypeList(request: HttpServletRequest, model: Model): List<CmdbTypeListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        return ciTypeService.getCmdbTypeList(params)
    }

    /**
     * CI Type 상세 정보 조회
     */
    @GetMapping("/{typeId}")
    fun getCmdbTypes(@PathVariable typeId: String): String {
        return ciTypeService.getCmdbTypes(typeId)
    }
}
