/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.dashboard.dto.DashboardGroupCountDto
import co.brainz.itsm.dashboard.dto.DashboardSearchCondition
import com.querydsl.core.QueryResults

interface DashboardRepositoryCustom : AliceRepositoryCustom {
    fun findTodoStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto>
    fun findRunningStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto>
    fun findMonthDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto>
    fun findDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto>
}
