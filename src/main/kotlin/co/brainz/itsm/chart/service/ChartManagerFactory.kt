/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.service.impl.ActivityGauge
import co.brainz.itsm.chart.service.impl.BasicLine
import co.brainz.itsm.chart.service.impl.LineAndColumn
import co.brainz.itsm.chart.service.impl.Pie
import co.brainz.itsm.chart.service.impl.StackedBar
import co.brainz.itsm.chart.service.impl.StackedColumn
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.token.service.TokenService
import org.springframework.stereotype.Component

@Component
class ChartManagerFactory(
    private val chartManagerService: ChartManagerService,
    private val instanceService: InstanceService,
    private val documentService: DocumentService,
    private val formService: FormService,
    private val tokenService: TokenService
) {

    fun getChartManager(chartType: String): ChartManager {
        return when (chartType) {
            ChartConstants.Type.BASIC_LINE.code -> BasicLine(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            ChartConstants.Type.PIE.code -> Pie(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            ChartConstants.Type.STACKED_COLUMN.code -> StackedColumn(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            ChartConstants.Type.STACKED_BAR.code -> StackedBar(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            ChartConstants.Type.LINE_AND_COLUMN.code -> LineAndColumn(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            ChartConstants.Type.ACTIVITY_GAUGE.code -> ActivityGauge(
                chartManagerService,
                instanceService,
                documentService,
                formService,
                tokenService
            )
            else -> throw AliceException(AliceErrorConstants.ERR, "ChartManager not found.")
        }
    }
}
