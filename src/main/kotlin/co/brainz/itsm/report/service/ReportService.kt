/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.itsm.report.dto.ReportListReturnDto
import co.brainz.itsm.report.dto.ReportSearchDto
import co.brainz.itsm.report.repository.ReportRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getReportList(reportSearchDto: ReportSearchDto): ReportListReturnDto {
        val reportList = reportRepository.getReportList(reportSearchDto)
        return ReportListReturnDto(
            data = reportList.results,
            totalCount = reportList.total
        )
    }
}
