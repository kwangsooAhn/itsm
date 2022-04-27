/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.CustomReportListDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.entity.QReportEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CusmtomReportRepositoryImpl : QuerydslRepositorySupport(ReportEntity::class.java), CusmtomReportRepositoryCustom {

    override fun getReportList(reportSearchCondition: ReportSearchCondition): Page<CustomReportListDto> {
        val report = QReportEntity.reportEntity
        val pageable = Pageable.unpaged()
        val query = from(report)
            .select(
                Projections.constructor(
                    CustomReportListDto::class.java,
                    report.reportId,
                    report.reportName,
                    report.reportDesc,
                    report.templateId,
                    report.publishDt
                )
            )
            .where(
                super.eq(report.templateId, reportSearchCondition.searchTemplate)
            )
            .orderBy(report.publishDt.desc())
        val totalCount = query.fetch().size
        if (reportSearchCondition.isPaging) {
            query.limit(reportSearchCondition.contentNumPerPage)
            query.offset((reportSearchCondition.pageNum - 1) * reportSearchCondition.contentNumPerPage)
        }
        return PageImpl<CustomReportListDto>(query.fetch(), pageable, totalCount.toLong())
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
