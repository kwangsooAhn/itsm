/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity

interface CustomReportTemplateRepositoryCustom : AliceRepositoryCustom {
    fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): PagingReturnDto
    fun getReportTemplateDetail(templateId: String): CustomReportTemplateEntity
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
