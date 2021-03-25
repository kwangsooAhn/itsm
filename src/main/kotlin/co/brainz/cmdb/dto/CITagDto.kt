/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CITagDto(
    val ciId: String? = "",
    val tagId: String? = "",
    var tagName: String? = null
) : Serializable
