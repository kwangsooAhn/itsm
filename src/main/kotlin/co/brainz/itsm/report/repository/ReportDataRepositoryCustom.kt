/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.report.entity.ReportDataEntity
import co.brainz.itsm.report.entity.ReportEntity

interface ReportDataRepositoryCustom : AliceRepositoryCustom {
    fun getReportDataEntitiesByReport(report: ReportEntity): List<ReportDataEntity>
}
