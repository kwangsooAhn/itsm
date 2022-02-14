/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.CustomReportListDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import com.querydsl.core.QueryResults

interface CusmtomReportRepositoryCustom : AliceRepositoryCustom {
    fun getReportList(reportSearchCondition: ReportSearchCondition): QueryResults<CustomReportListDto>
    fun getDistinctReportCategoryList(): List<ReportCategoryDto>
}
