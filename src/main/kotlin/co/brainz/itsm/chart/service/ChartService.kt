/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.respository.ChartRepository
import java.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChartService(
    private val chartRepository: ChartRepository,
    private val chartManagerFactory: ChartManagerFactory
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 사용자 정의 차트 조회
     */
    fun getCharts(searchTypeName: String, offset: String): ChartListReturnDto {
        return chartRepository.findChartList(searchTypeName, offset.toLong())
    }

    /**
     * 미리보기 조회
     */
    fun getChartPreviewDetail(chartId: String, chartPreviewDto: ChartDto): ChartDto {
        val chart = chartRepository.findByIdOrNull(chartId)
        val chartConfigStr =
            chartManagerFactory.getChartManager(chartPreviewDto.chartType).getChartConfigStr(chartPreviewDto)
        val chartDto = ChartDto(
            chartType = chartPreviewDto.chartType,
            chartName = chartPreviewDto.chartName,
            chartDesc = chartPreviewDto.chartDesc,
            chartConfig = chartConfigStr
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
            chartConfig = chart.chartConfig,
            createDt = chart.createDt
        )

        return chartManagerFactory.getChartManager(chart.chartType).getChart(chartDto)
    }

    /**
     * 사용자 정의 차트 등록 / 수정
     */
    fun saveChart(chartDto: ChartDto): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code
        val chartConfigStr = chartManagerFactory.getChartManager(chartDto.chartType).getChartConfigStr(chartDto)
        val chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = chartConfigStr
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
}
