/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class TemplateDto(
    val templateId: String,
    val result: Any? = null
) : Serializable
