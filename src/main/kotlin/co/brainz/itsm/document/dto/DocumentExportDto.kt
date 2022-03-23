/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import co.brainz.workflow.provider.dto.RestTemplateElementDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import java.io.Serializable

data class DocumentExportDto(
    val documentId: String = "",
    var process: RestTemplateProcessViewDto? = null,
    var elements: MutableList<RestTemplateElementDto>? = null,
    val form: RestTemplateFormDataDto,
    val displays: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
