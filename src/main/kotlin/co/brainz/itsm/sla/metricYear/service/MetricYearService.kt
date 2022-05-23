/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricStatus.dto.MetricAnnualDto
import co.brainz.itsm.sla.metricStatus.dto.MetricAnnualListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricYearService(
    private val metricYearRepository: MetricYearRepository,
    private val metricPoolRepository: MetricPoolRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceMessageSource: AliceMessageSource,
    private val excelComponent: ExcelComponent
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 연도별 SLA 지표 목록 조회
     */
    fun getMetrics(metricYearSearchCondition: MetricYearSearchCondition): MetricYearListReturnDto {
        val pagingResult = metricYearRepository.findMetrics(metricYearSearchCondition)

        return MetricYearListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = metricYearSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / metricYearSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 연도별 지표 신규 등록
     */
    @Transactional
    fun createMetricYear(metricYearDto: MetricYearDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findByMetricId(metricYearDto.metricId)

        if (metricYearRepository.existsByMetricAndMetricYear(metricYearDto.metricId, metricYearDto.metricYear)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricYearRepository.save(
                MetricYearEntity(
                    metric = metricEntity,
                    metricYear = metricYearDto.metricYear,
                    minValue = metricYearDto.minValue,
                    maxValue = metricYearDto.maxValue,
                    weightValue = metricYearDto.weightValue,
                    owner = metricYearDto.owner,
                    comment = metricYearDto.comment,
                    createUserKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        }
        return ZResponse(
            status = status.code
        )
    }

    fun getYearSaveMetricList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }

    /**
     * 년도별 SLA 현황 목록 조회
     */
    fun findMetricAnnualSearch(metricYearSearchCondition: MetricYearSearchCondition): MetricAnnualListReturnDto {
        val pagingResult = metricYearRepository.findMetrics(metricYearSearchCondition)
        val dataList: List<MetricAnnualDto> = mapper.convertValue(pagingResult.dataList)
        val scoreCalculation = this.scoreCalculation(dataList)

        return MetricAnnualListReturnDto(
            data = scoreCalculation,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = metricYearSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / metricYearSearchCondition.contentNumPerPage).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code

            )
        )
    }

    //zql 구현이 되면 instance의 건수를 계산하여 결과값이 나온다.
    /**
     * zql.setZqlExpression(zqlString)
    .setFromDateTime(from)
    .setToDateTime(to)
    .instanceStatus(InstanceStatus.RUNNING) // FINISH가 기본값. (SLA는 FINISH 사용)
    .criteria(ZqlInstanceDateCriteria.FROM) // TO가 기본값. (SLA는 TO 사용)
    .count() //.sum(), .average(), .percentage()
     */
    private fun scoreCalculation(metricYearList: List<MetricAnnualDto>): List<MetricAnnualDto> {
        metricYearList.forEach {
            it.score = 11.0
        }
        return metricYearList
    }

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
