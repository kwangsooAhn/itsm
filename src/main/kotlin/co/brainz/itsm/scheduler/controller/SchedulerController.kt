/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.controller

import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.scheduler.dto.SchedulerSearchDto
import co.brainz.itsm.scheduler.service.SchedulerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/schedulers")
class SchedulerController(
    private val schedulerService: SchedulerService,
    private val codeService: CodeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val schedulerSearchPage: String = "scheduler/schedulerSearch"
    private val schedulerEditPage: String = "scheduler/schedulerEdit"
    private val schedulerListPage: String = "scheduler/schedulerList"
    private val schedulerViewPage: String = "scheduler/schedulerView"

    /**
     * 스케줄러 관리 검색 화면.
     */
    @GetMapping("/search")
    fun getSchedulerSearch(model: Model): String {
        return schedulerSearchPage
    }

    /**
     * 스케줄러 리스트 화면.
     */
    @GetMapping("")
    fun getSchedulerList(schedulerSearchDto: SchedulerSearchDto, model: Model): String {
        val result = schedulerService.getSchedulers(schedulerSearchDto)
        model.addAttribute("schedulerList", result)
        model.addAttribute("schedulerListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return schedulerListPage
    }

    /**
     * 스케줄러 신규 등록 화면.
     */
    @GetMapping("/new")
    fun getSchedulerNew(model: Model): String {
        model.addAttribute("taskTypeList", codeService.selectCodeByParent(AliceConstants.SCHEDULE_TASK_TYPE))
        model.addAttribute(
            "executeCycleTypeList",
            codeService.selectCodeByParent(AliceConstants.SCHEDULE_EXECUTE_CYCLE_TYPE)
        )
        return schedulerEditPage
    }

    /**
     * 스케줄러 보기 화면.
     */
    @GetMapping("/{taskId}/view")
    fun getSchedulerView(@PathVariable taskId: String, model: Model): String {
        model.addAttribute("taskTypeList", codeService.selectCodeByParent(AliceConstants.SCHEDULE_TASK_TYPE))
        model.addAttribute(
            "executeCycleTypeList",
            codeService.selectCodeByParent(AliceConstants.SCHEDULE_EXECUTE_CYCLE_TYPE)
        )
        return schedulerViewPage
    }

    /**
     * 스케줄러 등록/수정 화면.
     */
    @GetMapping("/{taskId}/edit")
    fun getSchedulerEdit(@PathVariable taskId: String, model: Model): String {
        model.addAttribute("taskTypeList", codeService.selectCodeByParent(AliceConstants.SCHEDULE_TASK_TYPE))
        model.addAttribute(
            "executeCycleTypeList",
            codeService.selectCodeByParent(AliceConstants.SCHEDULE_EXECUTE_CYCLE_TYPE)
        )
        model.addAttribute("schedule", schedulerService.getScheduler(taskId))
        return schedulerEditPage
    }
}
