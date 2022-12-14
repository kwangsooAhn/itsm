/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateMapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomReportTemplateMapRepository : JpaRepository<CustomReportTemplateMapEntity, String> {
    fun deleteReportTemplateMapEntityByTemplate(templateEntity: CustomReportTemplateEntity)
}
