/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

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
}
