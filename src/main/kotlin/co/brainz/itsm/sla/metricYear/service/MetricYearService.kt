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
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConstants
import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartDto
import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualDto
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearCopyDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntityPk
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
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
    private val excelComponent: ExcelComponent,
    private val metricStatusService: MetricStatusService
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

        if (metricYearRepository.existsByMetricAndMetricYear(metricYearDto.metricId, metricYearDto.year)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricYearRepository.save(
                MetricYearEntity(
                    metric = metricEntity,
                    metricYear = metricYearDto.year,
                    minValue = metricYearDto.minValue,
                    maxValue = metricYearDto.maxValue,
                    weightValue = metricYearDto.weightValue,
                    owner = metricYearDto.owner,
                    comment = metricYearDto.comment,
                    zqlString = metricYearDto.zqlString,
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
     * 년도 선택 시 해당년도에 저장된 지표목록 불러오기
     */
    fun getYearSaveMetricList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        when (metricLoadCondition.type) {
            MetricPoolConstants.MetricTypeCode.SIMPLE_MANUAL.code -> metricLoadCondition.type =
                MetricPoolConstants.MetricTypeCode.MANUAL.code
            MetricPoolConstants.MetricTypeCode.SIMPLE_AUTO.code -> metricLoadCondition.type =
                MetricPoolConstants.MetricTypeCode.AUTO.code
        }
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
     * 년도별 SLA 현황 목록 조회
     */
    fun findMetricAnnualSearch(metricYearSearchCondition: MetricYearSearchCondition): MetricAnnualListReturnDto {
        val pagingResult = metricYearRepository.findMetricStatusList(metricYearSearchCondition)
        val dataList: List<MetricAnnualDto> = mapper.convertValue(pagingResult.dataList)

        return MetricAnnualListReturnDto(
            data = this.scoreCalculation(dataList),
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

    /**
     * 연도별 지표 세부 정보 조회
     */
    fun getMetricYearDetail(metricId: String, year: String): MetricYearDetailDto {
        return metricYearRepository.findMetricYear(metricId, year)
    }

    /**
     * 연도별 지표 편집
     */
    @Transactional
    fun updateMetricYear(metricYearDto: MetricYearDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        if (!metricYearRepository.existsById(MetricYearEntityPk(metricYearDto.metricId, metricYearDto.year))) {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        } else {
            val metricYearEntity =
                metricYearRepository.findByMetricAndMetricYear(MetricPoolEntity(metricYearDto.metricId), metricYearDto.year)
            metricYearEntity.minValue = metricYearDto.minValue
            metricYearEntity.maxValue = metricYearDto.maxValue
            metricYearEntity.weightValue = metricYearDto.weightValue
            metricYearEntity.owner = metricYearDto.owner
            metricYearEntity.comment = metricYearDto.comment
            metricYearEntity.zqlString = metricYearDto.zqlString
            metricYearEntity.updateUserKey = currentSessionUser.getUserKey()
            metricYearEntity.updateDt = LocalDateTime.now()

            metricYearRepository.save(metricYearEntity)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 연도별 지표 삭제
     */
    fun deleteMetricYear(metricId: String, year: String): ZResponse {
        val status = ZResponseConstants.STATUS.SUCCESS
        metricYearRepository.deleteById(MetricYearEntityPk(metricId, year))
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 연도별 지표 복사하기
     */
    @Transactional
    fun metricYearCopy(metricYearCopyDto: MetricYearCopyDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // metricId가 있을 경우 해당 지표만 복사
        if (!metricYearCopyDto.metricId.isNullOrBlank()) {
            val metricYearEntity =
                metricYearRepository.findById(
                    MetricYearEntityPk(
                        metric = metricYearCopyDto.metricId, metricYear = metricYearCopyDto.source))

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
            // metricId가 없을 경우 전체 지표 복사 (중복 제외)
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

    fun metricPreviewChartData(metricId: String, year: String): MetricStatusChartDto {
        val metricStatusCondition = MetricStatusChartCondition(
            metricId = metricId,
            year = year,
            chartType = ChartConstants.Type.BASIC_LINE.code
        )
        return metricStatusService.getMetricStatusChartData(metricStatusCondition)
    }

    /**
     * 연도별 지표 복사하기 모달
     */
    fun getYears(): Set<String> {
        return metricYearRepository.getYears()
    }
}
