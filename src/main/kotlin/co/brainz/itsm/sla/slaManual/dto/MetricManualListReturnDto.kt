/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.slaManual.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricManualListReturnDto(
    val data: List<MetricManualListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
