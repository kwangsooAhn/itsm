/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class RequestCIVO(
    @JsonProperty("ci_id")
    val ciId: String,

    @JsonProperty("ci_no")
    val ciNo: String,

    @JsonProperty("ci_name")
    val ciName: String,

    @JsonProperty("ci_desc")
    val ciDesc: String? = null,

    @JsonProperty("ci_status")
    val ciStatus: String,

    @JsonProperty("type_id")
    val typeId: String,

    @JsonProperty("mapping_id")
    val mappingId: String,

    val interlink: Boolean = true,

    val actionType: String
) : Serializable
