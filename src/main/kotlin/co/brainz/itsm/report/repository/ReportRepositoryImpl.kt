/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.dto.ReportCategoryDto
import co.brainz.itsm.report.dto.ReportListDto
import co.brainz.itsm.report.dto.ReportSearchCondition
import co.brainz.itsm.report.entity.QReportEntity
import co.brainz.itsm.report.entity.ReportEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReportRepositoryImpl : QuerydslRepositorySupport(ReportEntity::class.java), ReportRepositoryCustom {

    override fun getReportList(reportSearchCondition: ReportSearchCondition): QueryResults<ReportListDto> {
        val report = QReportEntity.reportEntity
        val query = from(report)
            .select(
                Projections.constructor(
                    ReportListDto::class.java,
                    report.reportId,
                    report.reportName,
                    report.reportDesc,
                    report.templateId,
                    report.publishDt
                )
            )
            .where(
                super.like(report.templateId, reportSearchCondition.searchTemplate)
            )
            .orderBy(report.publishDt.desc())
        if (reportSearchCondition.isPaging) {
            query.limit(reportSearchCondition.contentNumPerPage)
            query.offset((reportSearchCondition.pageNum - 1) * reportSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    /**
     * 보고서 Select 조건 조회
     */
    override fun getDistinctReportCategoryList(): List<ReportCategoryDto> {
        val report = QReportEntity.reportEntity
        return from(report).distinct()
            .select(
                Projections.constructor(
                    ReportCategoryDto::class.java,
                    report.templateId,
                    report.reportName
                )
            )
            .orderBy(report.reportName.asc())
            .fetch()
    }
}
