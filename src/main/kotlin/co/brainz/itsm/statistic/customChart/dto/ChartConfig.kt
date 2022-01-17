/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto

import java.io.Serializable
import co.brainz.itsm.statistic.customChart.constants.ChartConstants

data class ChartConfig(
    var range: ChartRange,
    var operation: String = "",
    var periodUnit: String? = null,
    var documentStatus: String? = ChartConstants.DocumentStatus.ONLY_FINISH.code,
    var condition: String? = null
) : Serializable
