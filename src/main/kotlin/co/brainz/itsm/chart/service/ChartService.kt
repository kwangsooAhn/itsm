/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.respository.ChartRepository
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChartService(private val chartRepository: ChartRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 통계 차트 조회
     */
    fun getCharts(searchTypeName: String, offset: String): List<ChartListDto> {
        return chartRepository.findChartList(searchTypeName, offset.toLong())
    }

    /**
     * 단일 통계 차트 조회
     */
    fun getChart(chartId: String): ChartDto {
        val chart = chartRepository.getOne(chartId)

        var chartConfig = JsonParser().parse(chart.chartConfig)
        val targetLabel = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.FROM.property).asString
        val operation = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.OPERATION.property).asString
        val duration = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.DURATION.property).asString
        val durationDigit =
            JsonParser().parse(duration).asJsonObject.get(ChartConstants.ObjProperty.DIGIT.property).asString
        val durationUnit =
            JsonParser().parse(duration).asJsonObject.get(ChartConstants.ObjProperty.UNIT.property).asString
        val periodUnit = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.PERIODUNIT.property).asString

        return ChartDto(
            chartId = chart.chartId,
            chartType = chart.chartType,
            chartName = chart.chartName,
            chartDesc = chart.chartDesc,
            chartConfig = chart.chartConfig,
            targetLabel = targetLabel,
            operation = operation,
            durationDigit = durationDigit.toInt(),
            durationUnit = durationUnit,
            periodUnit = periodUnit
        )
    }

    /**
     * 통계 차트 등록 / 수정
     */
    fun saveChart(chartDto: ChartDto): String {
        var status = ChartConstants.Status.STATUS_SUCCESS.code
        var chartConfigObj = JsonObject()
        var durationObj = JsonObject()
        var chartTypeList = JsonArray()
        chartTypeList.add(chartDto.chartType)
        // type
        chartConfigObj.addProperty(ChartConstants.ObjProperty.TYPE.property, chartTypeList.toString())
        // from
        chartConfigObj.addProperty(ChartConstants.ObjProperty.FROM.property, chartDto.targetLabel)
        // operation
        chartConfigObj.addProperty(ChartConstants.ObjProperty.OPERATION.property, chartDto.operation)
        // duration
        durationObj.addProperty(ChartConstants.ObjProperty.DIGIT.property, chartDto.durationDigit)
        durationObj.addProperty(ChartConstants.ObjProperty.UNIT.property, chartDto.durationUnit)
        chartConfigObj.addProperty(ChartConstants.ObjProperty.DURATION.property, durationObj.toString())
        // periodUnit
        chartConfigObj.addProperty(ChartConstants.ObjProperty.PERIODUNIT.property, chartDto.periodUnit)

        val chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = chartConfigObj.toString()
        )

        chartRepository.save(chartEntity)
        return status
    }

    /**
     * 통계 차트 삭제
     */
    fun deleteChart(chartId: String): String {
        var status = ChartConstants.Status.STATUS_SUCCESS.code

        chartRepository.deleteById(chartId)
        return status
    }
}
