package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTagDto(
    var tagId: String? = null,
    var instanceId: String = "",
    var tagContent: String = ""
) : Serializable
    