/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable

data class ViewerDataDto(
    val viewerKey: String,
    val reviewYn: Boolean = false,
    val displayYn: Boolean = false,
    val viewerType: String
) : Serializable
