/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomReportTemplateRepository : JpaRepository<CustomReportTemplateEntity, String>, CustomReportTemplateRepositoryCustom {
    fun findByTemplateId(templateId: String): CustomReportTemplateEntity?
}
