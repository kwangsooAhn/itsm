/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.api.dto

import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import java.io.Serializable

data class RequestEventDto(
    val documentId: String,
    val assigneeId: String,
    val default: List<RestTemplateTokenDataDto>
) : Serializable
