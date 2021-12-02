/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto.average

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable

data class ChartTagTokenData(
    val tag: AliceTagDto,
    val tokenDataList: List<ChartTokenData> = emptyList()
) : Serializable
