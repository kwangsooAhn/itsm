/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.itsm.statistic.customReport.entity.QCustomReportDataEntity
import co.brainz.itsm.statistic.customReport.entity.CustomReportDataEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CustomReportDataRepositoryImpl : QuerydslRepositorySupport(CustomReportDataEntity::class.java), CustomReportDataRepositoryCustom {

    override fun getReportDataEntitiesByReport(report: ReportEntity): List<CustomReportDataEntity> {
        val reportData = QCustomReportDataEntity.customReportDataEntity
        return from(reportData)
            .where(reportData.report.eq(report))
            .orderBy(reportData.displayOrder.asc())
            .fetch()
    }
}
