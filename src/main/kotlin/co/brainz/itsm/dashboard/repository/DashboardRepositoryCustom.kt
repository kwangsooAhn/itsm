/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.dashboard.dto.DashboardGroupCountDto
import co.brainz.itsm.dashboard.dto.DashboardSearchCondition

interface DashboardRepositoryCustom : AliceRepositoryCustom {
    fun findTodoStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto>
    fun findRunningStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto>
    fun findMonthDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto>
    fun findDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): List<DashboardGroupCountDto>
}
