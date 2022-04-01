/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.api.dto

import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import java.io.Serializable

data class RequestCmdbDto(
    val documentId: String,
    val assigneeId: String,
    val targetComponentId: String,
    var ciData: MutableList<RequestCIVO> = mutableListOf(),
    val ciComponentData: List<RequestCIComponentVO>,
    val default: List<RestTemplateTokenDataDto>
) : Serializable
