/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.dto

import java.io.Serializable

data class AliceTagDto(
    var tagId: String? = null,
    var tagType: String = "",
    var tagValue: String = "",
    var targetId: String = ""
) : Serializable
