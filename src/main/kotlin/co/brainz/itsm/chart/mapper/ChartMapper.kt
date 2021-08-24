/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.mapper

import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.entity.ChartEntity
import org.mapstruct.Mapper

@Mapper
interface ChartMapper {
    fun toChartDto(chartEntity: ChartEntity): ChartDto
}
