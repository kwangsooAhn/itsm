/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable

data class ChartTagCount(
    val tag: AliceTagDto,
    val count: Int = 0
) : Serializable
