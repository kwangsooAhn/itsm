/**
 * 폼 디자이너와 주고 받는 폼 데이터 구조를 표현한 DTO.
 *
 * @author Jung Hee Chan
 * @since 2021-05-14
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.provider.dto

import co.brainz.framework.tag.dto.AliceTagDto
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateFormDataDto(
    var id: String? = "",
    var name: String = "",
    var status: String? = "",
    var desc: String? = "",
    var category: String = "",
    var display: LinkedHashMap<String, Any>? = LinkedHashMap(),
    var updateDt: LocalDateTime?,
    var updateUserKey: String?,
    var createDt: LocalDateTime?,
    var createUserKey: String?,
    val createdWorkFlow: Boolean,
    val group: MutableList<FormGroupDto>? = mutableListOf()
) : Serializable

data class FormGroupDto(
    var id: String = "",
    var name: String? = "",
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var displayType: String? = null,
    var display: LinkedHashMap<String, Any> = LinkedHashMap(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var label: LinkedHashMap<String, Any>? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var row: MutableList<FormRowDto> = mutableListOf()
) : Serializable

data class FormRowDto(
    var id: String = "",
    var display: LinkedHashMap<String, Any> = LinkedHashMap(),
    val component: MutableList<FormComponentDto> = mutableListOf()
) : Serializable

data class FormComponentDto(
    var id: String = "",
    val type: String = "",
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var mapId: String = "",
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var value: String? = null,
    @get:JsonProperty(value = "isTopic")
    var isTopic: Boolean = false,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var tags: List<AliceTagDto>? = null,
    var display: LinkedHashMap<String, Any> = LinkedHashMap(),
    var label: LinkedHashMap<String, Any> = LinkedHashMap(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var validation: LinkedHashMap<String, Any>? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var element: LinkedHashMap<String, Any>? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var plugin: LinkedHashMap<String, Any>? = null
) : Serializable
