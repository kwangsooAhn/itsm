/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.itsm.statistic.customReport.dto.CustomTemplateCondition
import co.brainz.itsm.statistic.customReport.entity.QCustomTemplateEntity
import co.brainz.itsm.statistic.customReport.entity.CustomTemplateEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomTemplateRepositoryImpl : QuerydslRepositorySupport(CustomTemplateEntity::class.java),
    CustomTemplateRepositoryCustom {

    /**
     * 템플릿 조회
     */
    override fun getReportTemplateList(customTemplateCondition: CustomTemplateCondition): QueryResults<CustomTemplateEntity> {
        val template = QCustomTemplateEntity.customTemplateEntity
        val query = from(template)
            .where(
                super.likeIgnoreCase(template.templateName, customTemplateCondition.searchValue)
            )
            .orderBy(template.templateName.asc())
        if (customTemplateCondition.isPaging) {
            query.limit(customTemplateCondition.contentNumPerPage)
            query.offset((customTemplateCondition.pageNum - 1) * customTemplateCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    /**
     * 템플릿 상세 조회
     */
    override fun getReportTemplateDetail(templateId: String): CustomTemplateEntity {
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
