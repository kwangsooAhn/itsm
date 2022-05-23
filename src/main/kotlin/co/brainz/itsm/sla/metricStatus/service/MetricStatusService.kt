/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository,
    private val aliceMessageSource: AliceMessageSource,
    private val excelComponent: ExcelComponent
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     *  년도별 SLA 현황 엑셀 다운로드
     */
    fun getMetricExcelDownload(metricYearSearchCondition: MetricYearSearchCondition): ResponseEntity<ByteArray> {
        val returnDto = metricYearRepository.findMetricYearListForExcel(metricYearSearchCondition)
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.group"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.name"),
                                    cellWidth = 7000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.minValue"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.maxValue"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.weightValue"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.score"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.owner"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("sla.metric.label.comment"),
                                    cellWidth = 10000
                                )
                            )
                        )
                    )
                )
            )
        )
        returnDto.forEach { result ->
            excelVO.sheets[0].rows.add(
                ExcelRowVO(
                    cells = mutableListOf(
                        ExcelCellVO(value = result.metricGroupName ?: ""),
                        ExcelCellVO(value = result.metricName ?: ""),
                        ExcelCellVO(value = result.minValue ?: ""),
                        ExcelCellVO(value = result.maxValue ?: ""),
                        ExcelCellVO(value = result.weightValue ?: ""),
                        ExcelCellVO(value = result.score ?: ""),
                        ExcelCellVO(value = result.owner ?: ""),
                        ExcelCellVO(value = result.comment ?: "")
                    )
                )
            )
        }
        return excelComponent.download(excelVO)
    }
}
