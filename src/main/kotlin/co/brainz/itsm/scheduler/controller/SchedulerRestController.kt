/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.service.SchedulerService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/schedulers")
class SchedulerRestController(
    private val schedulerService: SchedulerService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Scheduler 등록.
     */
    @PostMapping("")
    fun createScheduler(@RequestBody schedulerDto: SchedulerDto): String {
        return schedulerService.insertScheduler(schedulerDto)
    }

    /**
     * Scheduler 수정.
     */
    @PutMapping("/{taskId}")
    fun updateScheduler(@RequestBody schedulerDto: SchedulerDto): String {
        return schedulerService.updateScheduler(schedulerDto)
    }

    /**
     * Scheduler 삭제.
     */
    @DeleteMapping("/{taskId}")
    fun deleteScheduler(@PathVariable taskId: String): String {
        return schedulerService.deleteScheduler(taskId)
    }

    /**
     * Scheduler 즉시 실행.
     */
    @PostMapping("/{taskId}/execute")
    fun immediateExecuteScheduler(@RequestBody schedulerDto: SchedulerDto): String {
        return schedulerService.immediateExecuteScheduler(schedulerDto)
    }

    /**
     * 스케줄러 최초 실행 시각 조회
     */
    @GetMapping("/{taskId}/executeTime")
    fun getSchedulerExecuteHistory(@PathVariable taskId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(schedulerService.getSchedulerExecuteHistory(taskId))
    }
}
