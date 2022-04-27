/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.entity.QCustomReportTemplateEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomReportTemplateRepositoryImpl : QuerydslRepositorySupport(CustomReportTemplateEntity::class.java),
    CustomReportTemplateRepositoryCustom {

    /**
     * 템플릿 조회
     */
    override fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): Page<CustomReportTemplateEntity> {
        val template = QCustomReportTemplateEntity.customReportTemplateEntity
        val pageable = Pageable.unpaged()
        val query = from(template)
            .where(
                super.likeIgnoreCase(template.templateName, customReportTemplateCondition.searchValue)
            )
            .orderBy(template.templateName.asc())
        val totalCount = query.fetch().size
        if (customReportTemplateCondition.isPaging) {
            query.limit(customReportTemplateCondition.contentNumPerPage)
            query.offset((customReportTemplateCondition.pageNum - 1) * customReportTemplateCondition.contentNumPerPage)
        }

        return PageImpl<CustomReportTemplateEntity>(query.fetch(), pageable, totalCount.toLong())
    }

    /**
     * 템플릿 상세 조회
     */
    override fun getReportTemplateDetail(templateId: String): CustomReportTemplateEntity {
        val template = QCustomReportTemplateEntity.customReportTemplateEntity
        return from(template)
            .where(template.templateId.eq(templateId))
            .fetchOne()
    }

    /**
     * Template 명 중복 체크
     */
    override fun findDuplicationTemplateName(templateName: String, templateId: String): Long {
        val template = QCustomReportTemplateEntity.customReportTemplateEntity
        val query = from(template)
            .where(template.templateName.eq(templateName))
        if (templateId.isNotEmpty()) {
            query.where(!template.templateId.eq(templateId))
        }
        return query.fetch().size.toLong()
    }
}
