/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.entity.ReportTemplateEntity
import co.brainz.itsm.report.entity.ReportTemplateMapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportTemplateMapRepository : JpaRepository<ReportTemplateMapEntity, String> {
    fun deleteReportTemplateMapEntityByTemplate(templateEntity: ReportTemplateEntity)
}
