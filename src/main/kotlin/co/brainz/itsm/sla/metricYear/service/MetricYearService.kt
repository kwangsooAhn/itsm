/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.service

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
import co.brainz.itsm.sla.metricManual.repository.MetricManualRepository
import co.brainz.itsm.sla.metricManual.service.MetricManualService
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConst
import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusDto
import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualDto
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearCopyDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearData
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearListReturnDto
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntityPk
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.zql.const.ZqlInstanceDateCriteria
import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.service.Zql
import co.brainz.workflow.instance.constants.InstanceStatus
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricYearService(
    private val metricYearRepository: MetricYearRepository,
    private val metricPoolRepository: MetricPoolRepository,
    private val metricManualRepository: MetricManualRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceMessageSource: AliceMessageSource,
    private val excelComponent: ExcelComponent,
    private val metricStatusService: MetricStatusService,
    private val metricManualService: MetricManualService,
    private val zql: Zql
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * ????????? SLA ?????? ?????? ??????
     */
    fun getMetrics(year: String): MetricYearListReturnDto {
        val result = metricYearRepository.findMetrics(year)

        return MetricYearListReturnDto(
            data = result,
            paging = AlicePagingData(
                totalCount = result.size.toLong(),
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = 0L,
                totalPageNum = 0L,
                orderType = ""
            )
        )
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    @Transactional
    fun createMetricYear(metricYearData: MetricYearData): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findByMetricId(metricYearData.metricId)

        if (metricYearRepository.existsByMetricAndMetricYear(metricYearData.metricId, metricYearData.year)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricYearRepository.save(
                MetricYearEntity(
                    metric = metricEntity,
                    metricYear = metricYearData.year,
                    minValue = metricYearData.minValue,
                    maxValue = metricYearData.maxValue,
                    weightValue = metricYearData.weightValue,
                    owner = metricYearData.owner,
                    comment = metricYearData.comment,
                    zqlString = metricYearData.zqlString,
                    createUserKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ?????? ?????? ??? ??????????????? ????????? ???????????? ????????????
     */
    fun getYearSaveMetricList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        val metricList = metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
        val metricIds: MutableSet<String> = mutableSetOf()
        metricList.forEach { metricIds.add(it.metricId) }

        if (!metricLoadCondition.target.isNullOrEmpty()) {
            val metricYearIds: LinkedHashSet<String> = linkedSetOf()
            metricYearRepository.findByMetricYear(metricLoadCondition.target!!).forEach {
                metricYearIds.add(it.metric.metricId)
            }
            metricIds.removeAll(metricYearIds)
        }

        return metricPoolRepository.findByMetricIds(metricIds)
    }

    /**
     * ????????? SLA ?????? ?????? ??????
     */
    fun findMetricAnnualSearch(year: String): MetricAnnualListReturnDto {
        var metricAnnualDtoList = metricYearRepository.findMetricStatusList(year)
        metricAnnualDtoList = this.scoreCalculation(metricAnnualDtoList, year)
        return MetricAnnualListReturnDto(
            data = metricAnnualDtoList,
            paging = AlicePagingData(
                totalCount = metricAnnualDtoList.size.toLong(),
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = 0L,
                totalPageNum = 0L,
                orderType = ""
            )
        )
    }

    /**
     * ????????? SLA ?????? ????????? ??????
     */
    private fun scoreCalculation(metricAnnualDtoList: List<MetricAnnualDto>, year: String): List<MetricAnnualDto> {
        val from = LocalDateTime.of(year.toInt(), 1, 1, 0, 0, 0)
        val to = LocalDateTime.of(year.toInt(), 12, 31, 23, 59, 59)
        metricAnnualDtoList.forEach {
            it.score = if (it.metricType == MetricPoolConst.Type.MANUAL.code) {
                when (it.calculationType) {
                    MetricPoolConst.CalculationType.SUM.code -> metricManualService.getManualPointSum(
                        it.metricId,
                        from.toLocalDate(),
                        to.toLocalDate()
                    )
                    MetricPoolConst.CalculationType.AVERAGE.code,
                    MetricPoolConst.CalculationType.PERCENTAGE.code -> metricManualService.getManualPointAverage(
                        it.metricId,
                        from.toLocalDate(),
                        to.toLocalDate()
                    )
                    else -> 0f
                }
            } else {
                zql.setExpression(it.zqlString)
                    .setFrom(from)
                    .setTo(to)
                    .setPeriod(ZqlPeriodType.YEAR)
                    .setInstanceStatus(InstanceStatus.FINISH)
                    .setCriteria(ZqlInstanceDateCriteria.END)

                when (it.calculationType) {
                    MetricPoolConst.CalculationType.SUM.code -> zql.sum()[0].value
                    MetricPoolConst.CalculationType.PERCENTAGE.code -> zql.percentage()[0].value
                    MetricPoolConst.CalculationType.AVERAGE.code -> zql.average()[0].value
                    else -> 0f
                }
            }
        }
        return metricAnnualDtoList
    }

    /**
     *  ????????? SLA ?????? ?????? ????????????
     */
    fun getMetricExcelDownload(year: String): ResponseEntity<ByteArray> {
        var returnDto = metricYearRepository.findMetricStatusList(year)
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
        returnDto = this.scoreCalculation(returnDto, year)
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

    /**
     * ????????? ?????? ?????? ?????? ??????
     */
    fun getMetricYearDetail(metricId: String, year: String): MetricYearDetailDto? {
        return metricYearRepository.findMetricYear(metricId, year)
    }

    /**
     * ????????? ?????? ??????
     */
    @Transactional
    fun updateMetricYear(metricYearData: MetricYearData): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        if (!metricYearRepository.existsById(MetricYearEntityPk(metricYearData.metricId, metricYearData.year))) {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        } else {
            val metricYearEntity =
                metricYearRepository.findByMetricAndMetricYear(
                    MetricPoolEntity(metricYearData.metricId),
                    metricYearData.year
                )
            metricYearEntity.minValue = metricYearData.minValue
            metricYearEntity.maxValue = metricYearData.maxValue
            metricYearEntity.weightValue = metricYearData.weightValue
            metricYearEntity.owner = metricYearData.owner
            metricYearEntity.comment = metricYearData.comment
            metricYearEntity.zqlString = metricYearData.zqlString
            metricYearEntity.updateUserKey = currentSessionUser.getUserKey()
            metricYearEntity.updateDt = LocalDateTime.now()

            metricYearRepository.save(metricYearEntity)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ????????? ?????? ??????
     */
    fun deleteMetricYear(metricId: String, year: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        // ???????????? ???????????? ??????????????? ??????
        if (metricManualRepository.existsByMetric(metricId)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricYearRepository.deleteById(MetricYearEntityPk(metricId, year))
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ????????? ?????? ????????????
     */
    @Transactional
    fun metricYearCopy(metricYearCopyDto: MetricYearCopyDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // metricId??? ?????? ?????? ?????? ????????? ??????
        if (!metricYearCopyDto.metricId.isNullOrBlank()) {
            val metricYearEntity =
                metricYearRepository.findById(
                    MetricYearEntityPk(
                        metric = metricYearCopyDto.metricId, metricYear = metricYearCopyDto.source
                    )
                )

            when (metricYearEntity.isEmpty) {
                true -> status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
                false -> metricYearRepository.save(
                    MetricYearEntity(
                        metric = MetricPoolEntity(metricId = metricYearCopyDto.metricId),
                        metricYear = metricYearCopyDto.target,
                        minValue = metricYearEntity.get().minValue,
                        maxValue = metricYearEntity.get().maxValue,
                        weightValue = metricYearEntity.get().weightValue,
                        owner = metricYearEntity.get().owner,
                        comment = metricYearEntity.get().comment,
                        zqlString = metricYearEntity.get().zqlString,
                        createUserKey = currentSessionUser.getUserKey(),
                        createDt = LocalDateTime.now()
                    )
                )
            }
            // metricId??? ?????? ?????? ?????? ?????? ?????? (?????? ??????)
        } else {
            val metricSourceYearEntityList = metricYearRepository.findByMetricYear(metricYearCopyDto.source)
            val metricTargetYearEntityList = metricYearRepository.findByMetricYear(metricYearCopyDto.target)

            metricSourceYearEntityList.forEach { source ->
                if (!metricTargetYearEntityList.contains(source)) {
                    metricYearRepository.save(
                        MetricYearEntity(
                            metric = MetricPoolEntity(metricId = source.metric.metricId),
                            metricYear = metricYearCopyDto.target,
                            minValue = source.minValue,
                            maxValue = source.maxValue,
                            weightValue = source.weightValue,
                            owner = source.owner,
                            comment = source.comment,
                            zqlString = source.zqlString,
                            createUserKey = currentSessionUser.getUserKey(),
                            createDt = LocalDateTime.now()
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
     * ????????? SLA ?????? preview
     */
    fun metricPreviewChartData(metricId: String, year: String): MetricStatusDto? {
        val metricStatusCondition = MetricStatusCondition(
            metricId = metricId,
            year = year,
            chartType = ChartConstants.Type.BASIC_LINE.code
        )
        return metricStatusService.getMetricStatusChartData(metricStatusCondition)
    }

    /**
     * ????????? ????????? ?????? ?????? ????????????
     */
    fun getYears(): Set<String> {
        return metricYearRepository.getYears()
    }
}
