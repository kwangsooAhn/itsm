/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.dto.ReportCondition
import co.brainz.itsm.report.dto.ReportListDto
import co.brainz.itsm.report.entity.QReportEntity
import co.brainz.itsm.report.entity.ReportEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReportRepositoryImpl : QuerydslRepositorySupport(ReportEntity::class.java), ReportRepositoryCustom {

    override fun getReportList(reportCondition: ReportCondition): QueryResults<ReportListDto> {
        val report = QReportEntity.reportEntity
        return from(report)
            .select(
                Projections.constructor(
                    ReportListDto::class.java,
                    report.reportId,
                    report.reportName,
                    report.reportDesc,
                    report.template,
                    report.publishDt
                )
            )
            .where(
                super.like(report.template.templateId, reportCondition.searchTemplate)
            )
            .orderBy(report.publishDt.desc())
            .limit(reportCondition.contentNumPerPage)
            .offset((reportCondition.pageNum - 1) * reportCondition.contentNumPerPage)
            .fetchResults()
    }
}
