/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/portal")
class PortalRestController(
    private val portalService: PortalService
) {
    /**
     * 포탈 검색 리스트 호출 처리
     */
    @GetMapping("")
    fun getPortalList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        val totalCount = portalService.findPortalListOrSearchCount(portalSearchDto)[0].totalCount
        val portalResult = portalService.findPortalListOrSearchList(portalSearchDto)
        val portalResultList = mutableListOf<PortalDto>()
        for (data in portalResult) {
            val portalDto = PortalDto(
                portalTitle = data.portalTitle,
                portalContent = data.portalContent,
                createDt = data.createDt,
                updateDt = data.updateDt,
                tableName = data.tableName,
                totalCount = totalCount
            )
            portalResultList.add(portalDto)
        }
        return portalResultList
    }

    @GetMapping("/top")
    fun getTopList(@RequestParam(value = "limit") limit: Long): LinkedHashMap<String, Any> {
        return portalService.getTopList(limit)
    }
}
