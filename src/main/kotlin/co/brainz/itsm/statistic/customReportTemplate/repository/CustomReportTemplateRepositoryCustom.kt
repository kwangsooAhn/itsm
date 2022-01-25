/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import com.querydsl.core.QueryResults

interface CustomReportTemplateRepositoryCustom : AliceRepositoryCustom {
    fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): QueryResults<CustomReportTemplateEntity>
    fun getReportTemplateDetail(templateId: String): CustomReportTemplateEntity
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
