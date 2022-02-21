/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class PluginListReturnDto(
    val data: List<PluginListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
