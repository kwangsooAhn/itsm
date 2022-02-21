/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.dto

import java.io.Serializable

data class PluginListDto(
    val pluginId: String,
    val pluginName: String,
    val pluginType: String,
    val pluginLocation: String,
    val pluginCommand: String
) : Serializable
