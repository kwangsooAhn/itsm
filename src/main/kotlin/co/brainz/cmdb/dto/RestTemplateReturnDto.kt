/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class RestTemplateReturnDto(
    var code: String = "0",
    var status: Boolean = true
) : Serializable
