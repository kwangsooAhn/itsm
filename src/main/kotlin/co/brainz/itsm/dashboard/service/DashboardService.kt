/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.DashboardGroupCountDto
import co.brainz.itsm.dashboard.dto.DashboardSearchCondition
import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.dashboard.repository.DashboardRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.token.constants.WfTokenConstants
import com.querydsl.core.QueryResults
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val wfInstanceService: WfInstanceService,
    private val currentSessionUser: CurrentSessionUser,
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
     *  업무 통계 데이터 조회
     */
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        val dashboardStatisticDtoList = mutableListOf<DashboardStatisticDto>()
        val typeList = listOf(
            DashboardConstants.StatisticType.TODO.code,
            DashboardConstants.StatisticType.RUNNING.code,
            DashboardConstants.StatisticType.MONTHDONE.code,
            DashboardConstants.StatisticType.DONE.code
        )

        typeList.forEach { type ->
            val dashboardSearchCondition = DashboardSearchCondition(userKey = currentSessionUser.getUserKey())
            val dashboardCountList: QueryResults<DashboardGroupCountDto>
            dashboardCountList = when (type) {
                DashboardConstants.StatisticType.TODO.code -> this.getTodoStatistic(dashboardSearchCondition)
                DashboardConstants.StatisticType.RUNNING.code -> this.getRunningStatistic(dashboardSearchCondition)
                DashboardConstants.StatisticType.MONTHDONE.code -> this.getMonthDoneStatistic(dashboardSearchCondition)
                DashboardConstants.StatisticType.DONE.code -> this.getDoneStatistic(dashboardSearchCondition)
                else -> throw AliceException(
                    AliceErrorConstants.ERR_00005,
                    AliceErrorConstants.ERR_00005.message + "[Dashboard Statistic Type Error]"
                )
            }

            val dashboardStatisticDto = DashboardStatisticDto()
            dashboardStatisticDto.type = type
            dashboardCountList.results.forEach {
                dashboardStatisticDto.total += it.count
                when (it.groupType) {
                    DashboardConstants.DocumentGroup.INCIDENT.code -> dashboardStatisticDto.incident = it.count
                    DashboardConstants.DocumentGroup.INQUIRY.code -> dashboardStatisticDto.inquiry = it.count
                    DashboardConstants.DocumentGroup.REQUEST.code -> dashboardStatisticDto.request = it.count
                    else -> dashboardStatisticDto.etc += it.count
                }
            }
            dashboardStatisticDtoList.add(dashboardStatisticDto)
        }

        return dashboardStatisticDtoList
    }

    /**
     * 처리할 문서 통계
     */
    private fun getTodoStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.TODO)
        dashboardSearchCondition.tokenStatus =
            WfTokenConstants.getTargetTokenStatusGroup(WfTokenConstants.SearchType.TODO)
        return dashboardRepository.findTodoStatistic(dashboardSearchCondition)
    }

    /**
     * 진행 중 문서 통계
     */
    private fun getRunningStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.PROGRESS)
        return dashboardRepository.findRunningStatistic(dashboardSearchCondition)
    }

    /**
     * 월간 처리 통계
     */
    private fun getMonthDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
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
    private fun getDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        dashboardSearchCondition.instanceStatus =
            WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED)
        return dashboardRepository.findDoneStatistic(dashboardSearchCondition)
    }
}
