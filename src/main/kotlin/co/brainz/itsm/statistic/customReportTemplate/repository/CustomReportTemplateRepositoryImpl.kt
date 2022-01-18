/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReport.entity.QCustomTemplateEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomReportTemplateRepositoryImpl : QuerydslRepositorySupport(CustomReportTemplateEntity::class.java),
    CustomReportTemplateRepositoryCustom {

    /**
     * 템플릿 조회
     */
    override fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): QueryResults<CustomReportTemplateEntity> {
        val template = QCustomTemplateEntity.customTemplateEntity
        val query = from(template)
            .where(
                super.likeIgnoreCase(template.templateName, customReportTemplateCondition.searchValue)
            )
            .orderBy(template.templateName.asc())
        if (customReportTemplateCondition.isPaging) {
            query.limit(customReportTemplateCondition.contentNumPerPage)
            query.offset((customReportTemplateCondition.pageNum - 1) * customReportTemplateCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    /**
     * 템플릿 상세 조회
     */
    override fun getReportTemplateDetail(templateId: String): CustomReportTemplateEntity {
        val template = QCustomTemplateEntity.customTemplateEntity
        return from(template)
            .where(template.templateId.eq(templateId))
            .fetchOne()
    }

    /**
     * Template 명 중복 체크
     */
    override fun findDuplicationTemplateName(templateName: String, templateId: String): Long {
        val template = QCustomTemplateEntity.customTemplateEntity
        val query = from(template)
            .where(template.templateName.eq(templateName))
        if (templateId.isNotEmpty()) {
            query.where(!template.templateId.eq(templateId))
        }
        return query.fetchCount()
    }
}
