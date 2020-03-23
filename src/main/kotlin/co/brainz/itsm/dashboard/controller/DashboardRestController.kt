package co.brainz.itsm.dashboard.controller

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboards")
class DashboardRestController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    //대시보드 조회
    @GetMapping("/{userKey}/view")
    fun getDashboard(@PathVariable userKey: String): String {
//        return dashboardService.findInstanceCount(userKey)
        return "true"
    }
}
