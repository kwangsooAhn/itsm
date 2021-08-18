/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.entity.ReportDataEntity
import co.brainz.itsm.report.entity.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportDataRepository : JpaRepository<ReportDataEntity, String>, ReportDataRepositoryCustom {
    fun getReportDataEntitiesByReport(report: ReportEntity): List<ReportDataEntity>
}
