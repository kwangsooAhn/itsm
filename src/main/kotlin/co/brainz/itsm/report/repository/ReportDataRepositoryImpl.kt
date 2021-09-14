/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.entity.QReportDataEntity
import co.brainz.itsm.report.entity.ReportDataEntity
import co.brainz.itsm.report.entity.ReportEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ReportDataRepositoryImpl : QuerydslRepositorySupport(ReportDataEntity::class.java), ReportDataRepositoryCustom {

    override fun getReportDataEntitiesByReport(report: ReportEntity): List<ReportDataEntity> {
        val reportData = QReportDataEntity.reportDataEntity
        return from(reportData)
            .where(reportData.report.eq(report))
            .orderBy(reportData.displayOrder.asc())
            .fetch()
    }
}
