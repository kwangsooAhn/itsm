/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import java.io.Serializable

data class DocumentImportDto(
    var documentData: DocumentEditDto,
    var processData: RestTemplateProcessElementDto,
    var formData: RestTemplateFormDataDto,
    var displayData: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
