/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.QCustomReportTemplateEntity
import com.querydsl.core.BooleanBuilder
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomReportTemplateRepositoryImpl : QuerydslRepositorySupport(CustomReportTemplateEntity::class.java),
    CustomReportTemplateRepositoryCustom {

    /**
     * 템플릿 조회
     */
    override fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): PagingReturnDto {
        val template = QCustomReportTemplateEntity.customReportTemplateEntity
        val query = from(template)
            .where(builder(template, customReportTemplateCondition))
        query.orderBy(template.templateName.asc())
        if (customReportTemplateCondition.isPaging) {
            query.limit(customReportTemplateCondition.contentNumPerPage)
            query.offset((customReportTemplateCondition.pageNum - 1) * customReportTemplateCondition.contentNumPerPage)
        }

        val countQuery = from(template)
            .select(template.count())
            .where(builder(template, customReportTemplateCondition))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    private fun builder(
        template: QCustomReportTemplateEntity,
        customReportTemplateCondition: CustomReportTemplateCondition
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(super.likeIgnoreCase(template.templateName, customReportTemplateCondition.searchValue))
        return builder
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
            .select(template.count())
            .where(template.templateName.eq(templateName))
        if (templateId.isNotEmpty()) {
            query.where(!template.templateId.eq(templateId))
        }
        return query.fetchOne()
    }
}
