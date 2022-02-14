/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.statistic.customReport.entity.CustomReportDataEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity

interface CustomReportDataRepositoryCustom : AliceRepositoryCustom {
    fun getReportDataEntitiesByReport(report: ReportEntity): List<CustomReportDataEntity>
}
