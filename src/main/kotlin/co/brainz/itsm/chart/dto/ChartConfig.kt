/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.itsm.chart.constants.ChartConstants
import java.io.Serializable

data class ChartConfig(
    var range: ChartRange,
    var operation: String = "",
    var periodUnit: String? = null,
    var documentStatus: String? = ChartConstants.DocumentStatus.ONLY_FINISH.code,
    var condition: String? = null
) : Serializable
