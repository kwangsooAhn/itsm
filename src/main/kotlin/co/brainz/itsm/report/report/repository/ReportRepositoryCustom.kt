/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.report.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.report.report.dto.ReportCategoryDto
import co.brainz.itsm.report.report.dto.ReportListDto
import co.brainz.itsm.report.report.dto.ReportSearchCondition
import com.querydsl.core.QueryResults

interface ReportRepositoryCustom : AliceRepositoryCustom {
    fun getReportList(reportSearchCondition: ReportSearchCondition): QueryResults<ReportListDto>
    fun getDistinctReportCategoryList(): List<ReportCategoryDto>
}
