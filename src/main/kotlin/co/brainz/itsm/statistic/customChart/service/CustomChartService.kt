/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.tag.service.AliceTagManager
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customChart.dto.CustomChartListDto
import co.brainz.itsm.statistic.customChart.dto.CustomChartListReturnDto
import co.brainz.itsm.statistic.customChart.entity.ChartEntity
import co.brainz.itsm.statistic.customChart.respository.CustomChartRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import kotlin.math.ceil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomChartService(
    private val customChartRepository: CustomChartRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val codeService: CodeService,
    private val aliceTagManager: AliceTagManager,
    private val aliceTagRepository: AliceTagRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    /**
     * ?????? ????????? ?????? ?????? ??????
     */
    fun getCharts(chartSearchCondition: ChartSearchCondition): CustomChartListReturnDto {
        val pagingResult = customChartRepository.findChartList(chartSearchCondition)
        return CustomChartListReturnDto(
            data = mapper.convertValue(pagingResult.dataList, object : TypeReference<List<CustomChartListDto>>() {}),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = customChartRepository.count(),
                currentPageNum = chartSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / chartSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code,
                orderColName = chartSearchCondition.orderColName,
                orderDir = chartSearchCondition.orderDir
            )
        )
    }

    /**
     * ???????????? ??????
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
     * ?????? ????????? ?????? ?????? ??????
     */
    fun getChartDetail(chartId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val chart = customChartRepository.findChartEntityByChartId(chartId)
        var chartDto: ChartDto? = null
        if (chart != null) {
            chartDto = ChartDto(
                chartId = chart.chartId,
                chartType = chart.chartType,
                chartName = chart.chartName,
                chartDesc = chart.chartDesc,
                chartConfig = mapper.readValue(chart.chartConfig, ChartConfig::class.java)
            )
            chartDto.tags = aliceTagManager.getTagsByTargetId(AliceTagConstants.TagType.CHART.code, chart.chartId)
            chartDto = chartManagerFactory.getChartManager(chart.chartType).getChart(chartDto)
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code,
            data = chartDto
        )
    }

    /**
     * ????????? ?????? ?????? ?????? / ??????
     */
    @Transactional
    fun saveChart(chartDto: ChartDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (customChartRepository.existsDuplicationData(chartDto)) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
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
                    aliceTagManager.insertTag(
                        AliceTagDto(
                            tagType = AliceTagConstants.TagType.CHART.code,
                            tagValue = tag.tagValue,
                            targetId = chartEntity.chartId
                        )
                    )
                }
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    @Transactional
    fun deleteChart(chartId: String): ZResponse {
        aliceTagRepository.deleteByTargetId(AliceTagConstants.TagType.CHART.code, chartId)
        customChartRepository.deleteById(chartId)
        return ZResponse()
    }

    /**
     * ?????? ???????????? ???????????? ?????? ?????????
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
