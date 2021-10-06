/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.dto.ChartSearchCondition
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.respository.ChartRepository
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import kotlin.math.ceil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChartService(
    private val chartRepository: ChartRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val codeService: CodeService
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 전체 사용자 정의 차트 조회
     */
    fun getCharts(chartSearchCondition: ChartSearchCondition): ChartListReturnDto {
        val queryResult = chartRepository.findChartList(chartSearchCondition)
        return ChartListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = chartRepository.count(),
                currentPageNum = chartSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 미리보기 조회
     */
    fun getChartPreviewDetail(chartId: String, chartPreviewDto: ChartDto): ChartDto {
        val chart = chartRepository.findByIdOrNull(chartId)
        val chartDto = ChartDto(
            chartType = chartPreviewDto.chartType,
            chartName = chartPreviewDto.chartName,
            chartDesc = chartPreviewDto.chartDesc,
            chartConfig = chartPreviewDto.chartConfig
        )
        if (chart != null) {
            chartDto.chartId = chart.chartId
            chartDto.createDt = chart.createDt
        } else {
            chartDto.createDt = LocalDateTime.now()
        }

        return chartManagerFactory.getChartManager(chartDto.chartType).getChart(chartDto)
    }

    /**
     * 단일 사용자 정의 차트 조회
     */
    fun getChartDetail(chartId: String): ChartDto {
        val chart = chartRepository.findById(chartId).get()
        val chartDto = ChartDto(
            chartId = chart.chartId,
            chartType = chart.chartType,
            chartName = chart.chartName,
            chartDesc = chart.chartDesc,
            chartConfig = mapper.readValue(chart.chartConfig, ChartConfig::class.java),
            createDt = chart.createDt
        )

        return chartManagerFactory.getChartManager(chart.chartType).getChart(chartDto)
    }

    /**
     * 사용자 정의 차트 등록 / 수정
     */
    fun saveChart(chartDto: ChartDto): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code
        val chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = mapper.writeValueAsString(chartDto.chartConfig)
        )

        chartRepository.save(chartEntity)
        return status
    }

    /**
     * 사용자 정의 차트 삭제
     */
    fun deleteChart(chartId: String): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code

        chartRepository.deleteById(chartId)
        return status
    }

    /**
     * 차트 설정에서 사용하는 코드 리스트
     */
    fun getCodeListForChart(): HashMap<String, MutableList<CodeDto>> {
        val codeListMap = HashMap<String, MutableList<CodeDto>>()
        codeListMap["type"] = codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code)
        codeListMap["operation"] = codeService.selectCodeByParent(ChartConstants.PCode.OPERATION.code)
        codeListMap["range"] = codeService.selectCodeByParent(ChartConstants.PCode.RANGE.code)
        codeListMap["unit"] = codeService.selectCodeByParent(ChartConstants.PCode.UNIT.code)

        return codeListMap
    }
}
