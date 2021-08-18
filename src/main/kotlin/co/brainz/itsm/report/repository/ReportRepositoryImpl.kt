/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.dto.ReportListDto
import co.brainz.itsm.report.dto.ReportSearchDto
import co.brainz.itsm.report.entity.QReportEntity
import co.brainz.itsm.report.entity.ReportEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReportRepositoryImpl : QuerydslRepositorySupport(ReportEntity::class.java), ReportRepositoryCustom {

    override fun getReportList(reportSearchDto: ReportSearchDto): QueryResults<ReportListDto> {
        val report = QReportEntity.reportEntity
        val query = from(report)
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
                super.like(report.template.templateId, reportSearchDto.search.toString())
            )
        query.orderBy(report.publishDt.desc())
        if (reportSearchDto.limit != null && reportSearchDto.limit > - 1) {
            query.limit(reportSearchDto.limit)
        }
        if (reportSearchDto.offset != null && reportSearchDto.offset > -1) {
            query.offset(reportSearchDto.offset)
        }
        return query.fetchResults()
    }
}
