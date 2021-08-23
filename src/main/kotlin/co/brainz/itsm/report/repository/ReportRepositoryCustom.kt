/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.report.dto.ReportCondition
import co.brainz.itsm.report.dto.ReportListDto
import com.querydsl.core.QueryResults

interface ReportRepositoryCustom : AliceRepositoryCustom {
    fun getReportList(reportCondition: ReportCondition): QueryResults<ReportListDto>
}
