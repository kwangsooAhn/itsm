/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service.impl

import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.service.ChartManager
import co.brainz.itsm.chart.service.ChartManagerService
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.token.service.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BasicLine(
    chartManagerService: ChartManagerService,
    instanceService: InstanceService,
    documentService: DocumentService,
    formService: FormService,
    tokenService: TokenService
) : ChartManager(chartManagerService, instanceService, documentService, formService, tokenService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun setChartConfigDetail(chartDto: ChartDto): LinkedHashMap<String, Any?> {
        val chartMap = LinkedHashMap<String, Any?>()
        chartMap[ChartConstants.ObjProperty.PERIOD_UNIT.property] = chartDto.chartConfig.periodUnit
        chartMap[ChartConstants.ObjProperty.GROUP.property] = chartDto.chartConfig.group
        return chartMap
    }

    override fun setChartDetail(chartDto: ChartDto): ChartDto {
        chartDto.chartConfig.periodUnit = super.chartConfig.periodUnit
        chartDto.chartConfig.group = super.chartConfig.group
        return chartDto
    }
}
