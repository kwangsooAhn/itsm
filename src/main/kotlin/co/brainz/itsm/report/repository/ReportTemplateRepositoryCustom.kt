/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.report.dto.ReportTemplateDto
import co.brainz.itsm.report.dto.ReportTemplateListDto
import co.brainz.itsm.report.dto.ReportTemplateSearchDto
import com.querydsl.core.QueryResults

interface ReportTemplateRepositoryCustom : AliceRepositoryCustom {

    fun getReportTemplateList(reportTemplateSearchDto: ReportTemplateSearchDto): QueryResults<ReportTemplateListDto>
    fun getReportTemplateDetail(templateId: String): ReportTemplateDto
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
