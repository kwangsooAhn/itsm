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
                //select option
            .limit(reportSearchDto.limit)
            .offset(reportSearchDto.offset)
            .fetchResults()
    }
}
