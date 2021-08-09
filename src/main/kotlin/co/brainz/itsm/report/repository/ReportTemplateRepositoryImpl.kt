/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.dto.ReportTemplateDto
import co.brainz.itsm.report.dto.ReportTemplateListDto
import co.brainz.itsm.report.dto.ReportTemplateSearchDto
import co.brainz.itsm.report.entity.QReportTemplateEntity
import co.brainz.itsm.report.entity.ReportTemplateEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReportTemplateRepositoryImpl : QuerydslRepositorySupport(ReportTemplateEntity::class.java),
    ReportTemplateRepositoryCustom {

    override fun getReportTemplateList(reportTemplateSearchDto: ReportTemplateSearchDto): QueryResults<ReportTemplateListDto> {
        val template = QReportTemplateEntity.reportTemplateEntity
        return from(template)
            .select(
                Projections.constructor(
                    ReportTemplateListDto::class.java,
                    template.templateId,
                    template.templateName,
                    template.templateDesc,
                    template.automatic,
                    template.createDt,
                    template.createUser.userName
                )
            )
            .where(
                super.like(template.templateName, reportTemplateSearchDto.search.toString())
            )
            .limit(reportTemplateSearchDto.limit)
            .offset(reportTemplateSearchDto.offset)
            .fetchResults()
    }

    /**
     * 템플릿 상세 조회
     */
    override fun getReportTemplateDetail(templateId: String): ReportTemplateDto {
        val template = QReportTemplateEntity.reportTemplateEntity
        return from(template)
            .select(
                Projections.constructor(
                    ReportTemplateDto::class.java,
                    template.templateId,
                    template.templateName,
                    template.templateDesc,
                    template.automatic
                )
            )
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
