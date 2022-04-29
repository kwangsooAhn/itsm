/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.CustomReportListDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.entity.QReportEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CusmtomReportRepositoryImpl : QuerydslRepositorySupport(ReportEntity::class.java), CusmtomReportRepositoryCustom {

    override fun getReportList(reportSearchCondition: ReportSearchCondition): PagingReturnDto {
        val report = QReportEntity.reportEntity
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
        if (reportSearchCondition.isPaging) {
            query.limit(reportSearchCondition.contentNumPerPage)
            query.offset((reportSearchCondition.pageNum - 1) * reportSearchCondition.contentNumPerPage)
        }

        val countQuery = from(report)
            .select(report.count())
            .where(super.eq(report.templateId, reportSearchCondition.searchTemplate))
        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
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
