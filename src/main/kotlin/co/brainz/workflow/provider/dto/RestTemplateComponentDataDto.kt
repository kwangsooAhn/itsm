/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateComponentDataDto(
    val attributeId: String,
    val attributeValue: Any
) : Serializable
