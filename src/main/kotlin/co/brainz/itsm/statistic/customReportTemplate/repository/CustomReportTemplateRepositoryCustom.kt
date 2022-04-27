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
import org.springframework.data.domain.Page

interface CustomReportTemplateRepositoryCustom : AliceRepositoryCustom {
    fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): Page<CustomReportTemplateEntity>
    fun getReportTemplateDetail(templateId: String): CustomReportTemplateEntity
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
