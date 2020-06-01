package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTagDto(
    var instanceId: String? = null,
    var tagId: String? = null,
    var tagContent: String? = null
) : Serializable
    