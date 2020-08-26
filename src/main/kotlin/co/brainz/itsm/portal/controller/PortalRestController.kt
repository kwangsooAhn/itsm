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
        return portalService.findPortalListOrSearchList(portalSearchDto)
    }

    @GetMapping("/top")
    fun getTopList(@RequestParam(value = "limit") limit: Long): LinkedHashMap<String, Any> {
        return portalService.getTopList(limit)
    }
}
