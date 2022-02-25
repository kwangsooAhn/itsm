/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.dto

data class PluginDto(
    val tokenId: String?,
    var pluginId: String,
    val data: Any?
)
