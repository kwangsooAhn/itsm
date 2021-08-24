/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.report.dto.ReportTemplateCondition
import co.brainz.itsm.report.entity.ReportTemplateEntity
import com.querydsl.core.QueryResults

interface ReportTemplateRepositoryCustom : AliceRepositoryCustom {
    fun getReportTemplateList(reportTemplateCondition: ReportTemplateCondition): QueryResults<ReportTemplateEntity>
    fun getReportTemplateDetail(templateId: String): ReportTemplateEntity
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
