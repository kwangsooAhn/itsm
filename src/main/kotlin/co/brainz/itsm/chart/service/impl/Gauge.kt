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
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Gauge(
    chartManagerService: ChartManagerService,
    instanceService: InstanceService,
    documentService: DocumentService,
    formService: FormService,
    tokenService: TokenService,
    wfTokenManagerService: WfTokenManagerService
) : ChartManager(
    chartManagerService,
    instanceService,
    documentService,
    formService,
    tokenService,
    wfTokenManagerService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun setChartConfigDetail(chartDto: ChartDto): LinkedHashMap<String, Any?> {
        val chartMap = LinkedHashMap<String, Any?>()
        chartMap[ChartConstants.ObjProperty.PERIOD_UNIT.property] = chartDto.chartConfig.periodUnit
        return chartMap
    }

    override fun setChartDetail(chartDto: ChartDto): ChartDto {
        chartDto.chartConfig.periodUnit = super.chartConfig.periodUnit
        return chartDto
    }
}
