/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.itsm.report.dto.ReportTemplateListReturnDto
import co.brainz.itsm.report.dto.ReportTemplateSearchDto
import co.brainz.itsm.report.repository.ReportTemplateRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportTemplateService(
    private val reportTemplateRepository: ReportTemplateRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getReportTemplateList(reportTemplateSearchDto: ReportTemplateSearchDto): ReportTemplateListReturnDto {
        val templateList = reportTemplateRepository.getReportTemplateList(reportTemplateSearchDto)
        return ReportTemplateListReturnDto(
            data = templateList.results,
            totalCount = templateList.total
        )
    }
}
