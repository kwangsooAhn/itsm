/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

data class CIComponentDataDto(
    val ciId: String = "",
    val componentId: String = "",
    val values: CIComponentDetail,
    val instanceId: String? = null
) : Serializable

class CIComponentDetail(
    val ciAttributes: LinkedHashMap<String, Any> = LinkedHashMap(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val ciTags: LinkedHashMap<String, Any>? = null
) : Serializable
