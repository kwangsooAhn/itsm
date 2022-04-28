/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.itsm.dashboard.dto.DashboardGroupCountDto
import co.brainz.itsm.dashboard.dto.DashboardSearchCondition
import co.brainz.itsm.dashboard.repository.DashboardRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.token.constants.WfTokenConstants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val wfInstanceService: WfInstanceService,
    private val dashboardRepository: DashboardRepository
) {

    /**
     * 신청한 문서 현황 count 조회
     *
     * @return
     */
    fun getStatusCountList(params: LinkedHashMap<String, Any>): List<RestTemplateInstanceCountDto> {
        return wfInstanceService.instancesStatusCount(params)
    }

    /**
     * 처리할 문서 통계
     */
    private fun getTodoStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.TODO)
        dashboardSearchCondition.tokenStatus =
            WfTokenConstants.getTargetTokenStatusGroup(WfTokenConstants.SearchType.TODO)
        return dashboardRepository.findTodoStatistic(dashboardSearchCondition)
    }

    /**
     * 진행 중 문서 통계
     */
    private fun getRunningStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.PROGRESS)
        return dashboardRepository.findRunningStatistic(dashboardSearchCondition)
    }

    /**
     * 월간 처리 통계
     */
    private fun getMonthDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED)

        // 월간 범위 지정
        val today = LocalDate.now()
        dashboardSearchCondition.searchFromDt = LocalDateTime.parse(
            LocalDateTime.of(today.year, today.month, 1, 0, 0, 0).toString(),
            DateTimeFormatter.ISO_DATE_TIME
        ).toString()
        dashboardSearchCondition.searchToDt =
            LocalDateTime.parse(
                LocalDateTime.of(today.year, today.month, today.lengthOfMonth(), 23, 59, 59)
                    .toString(), DateTimeFormatter.ISO_DATE_TIME
            ).toString()
        return dashboardRepository.findMonthDoneStatistic(dashboardSearchCondition)
    }

    /**
     * 전체 처리한 문서 통계
     */
    private fun getDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED)
        return dashboardRepository.findDoneStatistic(dashboardSearchCondition)
    }
}
