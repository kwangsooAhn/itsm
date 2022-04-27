/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition

interface CusmtomReportRepositoryCustom : AliceRepositoryCustom {
    fun getReportList(reportSearchCondition: ReportSearchCondition): PagingReturnDto
    fun getDistinctReportCategoryList(): List<ReportCategoryDto>
}
