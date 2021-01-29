/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.respository.ChartRepository
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
}
