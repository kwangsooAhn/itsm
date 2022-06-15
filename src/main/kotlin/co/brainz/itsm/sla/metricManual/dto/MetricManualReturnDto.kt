/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricManualReturnDto(
    val data: List<MetricManualDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
