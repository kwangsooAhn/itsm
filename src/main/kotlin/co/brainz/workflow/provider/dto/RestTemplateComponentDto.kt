/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateComponentDto(
    val componentId: String,
    val componentType: String,
    val formId: String,
    val componentData: List<RestTemplateComponentDataDto>
) : Serializable
