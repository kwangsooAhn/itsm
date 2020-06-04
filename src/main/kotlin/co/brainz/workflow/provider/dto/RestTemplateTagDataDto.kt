package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTagDataDto(
    var tagId: String? = "",
    var tagContent: String? = ""
) : Serializable
    