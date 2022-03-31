/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.api.dto

import java.io.Serializable

data class RequestCIComponentVO(
    val ciId: String,
    val componentId: String,
    var values: Any? = null,
    var instanceId: String
) : Serializable
