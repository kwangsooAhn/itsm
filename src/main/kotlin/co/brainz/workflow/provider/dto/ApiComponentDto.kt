/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import java.io.Serializable

data class ApiComponentDto(
    val componentId: String,
    val componentType: String,
    val mappingId: String,
    val formId: String,
    val formRowId: String,
    val properties: List<ComponentPropertyDto>
) : Serializable
