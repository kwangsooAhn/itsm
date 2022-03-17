/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import java.io.Serializable

data class DocumentImportDto(
    val documentData: DocumentDto,
    var processData: RestTemplateProcessElementDto,
    val formData: RestTemplateFormDataDto,
    val displayData: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
