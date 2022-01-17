/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.statistic.customReport.dto.CustomTemplateCondition
import co.brainz.itsm.statistic.customReport.entity.CustomTemplateEntity
import com.querydsl.core.QueryResults

interface CustomTemplateRepositoryCustom : AliceRepositoryCustom {
    fun getReportTemplateList(customTemplateCondition: CustomTemplateCondition): QueryResults<CustomTemplateEntity>
    fun getReportTemplateDetail(templateId: String): CustomTemplateEntity
    fun findDuplicationTemplateName(templateName: String, templateId: String): Long
}
