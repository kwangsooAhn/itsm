/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.type.controller

import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.itsm.cmdb.type.service.TypeService
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/cmdb/types")
class TypeRestController(private val typeService: TypeService) {

    /**
     * CI Type 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getCodes(request: HttpServletRequest, model: Model): List<CmdbTypeListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        return typeService.getTypes(params)
    }
}
