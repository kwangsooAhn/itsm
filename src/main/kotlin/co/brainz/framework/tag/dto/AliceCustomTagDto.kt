/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class AliceCustomTagDto(
    @JsonProperty("tagId")
    var tagId: String? = null,
    @JsonProperty("tagType", access = JsonProperty.Access.WRITE_ONLY)
    var tagType: String = "",
    @JsonProperty("value")
    var tagValue: String = "",
    @JsonProperty("targetId", access = JsonProperty.Access.WRITE_ONLY)
    var targetId: String = "",
    val options: Any? = null
) : Serializable
