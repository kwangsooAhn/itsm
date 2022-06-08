/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.dto

import java.io.Serializable

data class WfComponentTemplateDto(
    val templateId: String = "",
    val templateName: String,
    val type: String,
    val data: Any
) : Serializable
