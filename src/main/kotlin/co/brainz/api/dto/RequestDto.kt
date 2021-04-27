/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.api.dto

import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import java.io.Serializable

data class RequestDto(
    var documentId: String? = null,
    var instanceId: String = "",
    var tokenId: String = "",
    var isComplete: Boolean = true,
    var assigneeId: String? = null,
    var assigneeType: String? = null,
    val action: String? = null,
    var componentData: List<RestTemplateTokenDataDto>? = null,
    var optionData: Any? = null
) : Serializable
