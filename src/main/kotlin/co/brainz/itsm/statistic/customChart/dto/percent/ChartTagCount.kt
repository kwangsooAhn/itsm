/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto.percent

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable

data class ChartTagCount(
    val tag: AliceTagDto,
    val count: Int = 0,
    val conditionCount: Int = 0
) : Serializable
