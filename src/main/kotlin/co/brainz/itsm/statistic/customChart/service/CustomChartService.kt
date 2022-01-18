/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.statistic.customChart.entity.ChartEntity
import co.brainz.itsm.statistic.customChart.respository.CustomChartRepository
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import kotlin.math.ceil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.CustomChartListReturnDto
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition

@Service
class CustomChartService(
    private val customChartRepository: CustomChartRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val codeService: CodeService,
    private val aliceTagService: AliceTagService,
    private val aliceTagRepository: AliceTagRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    /**
     * 전체 사용자 정의 차트 조회
     */
    fun getCharts(chartSearchCondition: ChartSearchCondition): CustomChartListReturnDto {
        val queryResult = customChartRepository.findChartList(chartSearchCondition)
        return CustomChartListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = customChartRepository.count(),
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
        val chart = customChartRepository.findByIdOrNull(chartId)
        val chartDto = ChartDto(
            chartType = chartPreviewDto.chartType,
            chartName = chartPreviewDto.chartName,
            chartDesc = chartPreviewDto.chartDesc,
            chartConfig = chartPreviewDto.chartConfig,
            tags = chartPreviewDto.tags
        )
        if (chart != null) {
            chartDto.chartId = chart.chartId
        }

        return chartManagerFactory.getChartManager(chartDto.chartType).getChart(chartDto)
    }

    /**
     * 단일 사용자 정의 차트 조회
     */
    fun getChartDetail(chartId: String): ChartDto {
        val chart = customChartRepository.findChartEntityByChartId(chartId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Chart Entity]"
        )
        val chartDto = ChartDto(
            chartId = chart.chartId,
            chartType = chart.chartType,
            chartName = chart.chartName,
            chartDesc = chart.chartDesc,
            chartConfig = mapper.readValue(chart.chartConfig, ChartConfig::class.java)
        )
        chartDto.tags = aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.CHART.code, chart.chartId)

        return chartManagerFactory.getChartManager(chart.chartType).getChart(chartDto)
    }

    /**
     * 사용자 정의 차트 등록 / 수정
     */
    @Transactional
    fun saveChart(chartDto: ChartDto): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code
        var chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = mapper.writeValueAsString(chartDto.chartConfig)
        )
        chartEntity = customChartRepository.save(chartEntity)

        // tag save
        aliceTagRepository.deleteByTargetId(AliceTagConstants.TagType.CHART.code, chartEntity.chartId)
        aliceTagRepository.flush()

        if (chartDto.tags.isNotEmpty()) {
            chartDto.tags.forEach { tag ->
                aliceTagService.insertTag(
                    AliceTagDto(
                        tagType = AliceTagConstants.TagType.CHART.code,
                        tagValue = tag.tagValue,
                        targetId = chartEntity.chartId
                    )
                )
            }
        }
        return status
    }

    /**
     * 사용자 정의 차트 삭제
     */
    @Transactional
    fun deleteChart(chartId: String): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code
        aliceTagRepository.deleteByTargetId(AliceTagConstants.TagType.CHART.code, chartId)
        customChartRepository.deleteById(chartId)
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
        codeListMap["documentStatus"] = codeService.selectCodeByParent(ChartConstants.PCode.DOCUMENT_STATUS.code)

        return codeListMap
    }
}
