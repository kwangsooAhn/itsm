/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.Serializable

data class AliceTagDto(
    @JsonProperty("id")
    var tagId: String? = null,
    @JsonIgnore
    var tagType: String = "",
    @JsonProperty("value")
    var tagValue: String = "",
    @JsonIgnore
    var targetId: String = ""
) : Serializable {
    override fun toString(): String {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.writeValueAsString(this)
    }
}
