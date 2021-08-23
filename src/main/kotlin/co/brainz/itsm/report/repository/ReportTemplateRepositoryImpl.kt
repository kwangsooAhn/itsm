/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.dto.ReportTemplateCondition
import co.brainz.itsm.report.entity.QReportTemplateEntity
import co.brainz.itsm.report.entity.ReportTemplateEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReportTemplateRepositoryImpl : QuerydslRepositorySupport(ReportTemplateEntity::class.java),
    ReportTemplateRepositoryCustom {

    /**
     * 템플릿 조회
     */
    override fun getReportTemplateList(reportTemplateCondition: ReportTemplateCondition): QueryResults<ReportTemplateEntity> {
        val template = QReportTemplateEntity.reportTemplateEntity
        val query = from(template)
            .where(
                super.like(template.templateName, reportTemplateCondition.searchValue)
            )
            .orderBy(template.templateName.asc())
        if (reportTemplateCondition.isPaging) {
            query.limit(reportTemplateCondition.contentNumPerPage)
            query.offset((reportTemplateCondition.pageNum - 1) * reportTemplateCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    /**
     * 템플릿 상세 조회
     */
    override fun getReportTemplateDetail(templateId: String): ReportTemplateEntity {
        val template = QReportTemplateEntity.reportTemplateEntity
        return from(template)
            .where(template.templateId.eq(templateId))
            .fetchOne()
    }

    /**
     * Template 명 중복 체크
     */
    override fun findDuplicationTemplateName(templateName: String, templateId: String): Long {
        val template = QReportTemplateEntity.reportTemplateEntity
        val query = from(template)
            .where(template.templateName.eq(templateName))
        if (templateId.isNotEmpty()) {
            query.where(!template.templateId.eq(templateId))
        }
        return query.fetchCount()
    }
}
