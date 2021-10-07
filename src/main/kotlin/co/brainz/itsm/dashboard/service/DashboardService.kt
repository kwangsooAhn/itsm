/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.itsm.token.service.TokenService
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
    private val currentSessionUser: CurrentSessionUser,
    private val tokenService: TokenService
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
     *  업무통계 데이터 조회
     */
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        val dashboardStatisticDtoList = mutableListOf<DashboardStatisticDto>()
        val typeList = listOf(
            DashboardConstants.StatisticType.TODO.code,
            DashboardConstants.StatisticType.RUNNING.code,
            DashboardConstants.StatisticType.MONTHDONE.code,
            DashboardConstants.StatisticType.DONE.code
        )

        val tokenSearchCondition = TokenSearchCondition(
            userKey = currentSessionUser.getUserKey()
        )
        var dashboardStatisticDto: DashboardStatisticDto

        val today = LocalDate.now()

        for (i in 0 until typeList.size) {
            dashboardStatisticDto = DashboardStatisticDto()
            when (typeList[i]) {
                DashboardConstants.StatisticType.TODO.code -> {
                    tokenSearchCondition.searchTokenType = WfTokenConstants.SearchType.TODO.code
                }
                DashboardConstants.StatisticType.RUNNING.code -> {
                    tokenSearchCondition.searchTokenType = WfTokenConstants.SearchType.PROGRESS.code
                }
                DashboardConstants.StatisticType.MONTHDONE.code -> {
                    tokenSearchCondition.searchTokenType = WfTokenConstants.SearchType.COMPLETED.code
                    tokenSearchCondition.searchFromDt =
                        LocalDateTime.parse(
                            LocalDateTime.of(today.year, today.month, 1, 0, 0, 0).toString(),
                            DateTimeFormatter.ISO_DATE_TIME
                        ).toString()
                    tokenSearchCondition.searchToDt =
                        LocalDateTime.parse(
                            LocalDateTime.of(today.year, today.month, today.lengthOfMonth(), 23, 59, 59)
                                .toString(), DateTimeFormatter.ISO_DATE_TIME
                        ).toString()
                }
                DashboardConstants.StatisticType.DONE.code -> {
                    tokenSearchCondition.searchFromDt = ""
                    tokenSearchCondition.searchToDt = ""
                    tokenSearchCondition.searchTokenType = WfTokenConstants.SearchType.COMPLETED.code
                }
            }
            dashboardStatisticDto.type = typeList[i]
            tokenSearchCondition.documentGroup = ""
            dashboardStatisticDto.total = tokenService.getTokenList(tokenSearchCondition).totalCount
            tokenSearchCondition.documentGroup = DashboardConstants.DocumentGroup.INCIDENT.code
            dashboardStatisticDto.incident = tokenService.getTokenList(tokenSearchCondition).totalCount
            tokenSearchCondition.documentGroup = DashboardConstants.DocumentGroup.INQUIRY.code
            dashboardStatisticDto.inquiry = tokenService.getTokenList(tokenSearchCondition).totalCount
            tokenSearchCondition.documentGroup = DashboardConstants.DocumentGroup.REQUEST.code
            dashboardStatisticDto.request = tokenService.getTokenList(tokenSearchCondition).totalCount
            tokenSearchCondition.documentGroup = DashboardConstants.DocumentGroup.ETC.code
            dashboardStatisticDto.etc = tokenService.getTokenList(tokenSearchCondition).totalCount

            dashboardStatisticDtoList.add(dashboardStatisticDto)
        }
        return dashboardStatisticDtoList
    }
}
