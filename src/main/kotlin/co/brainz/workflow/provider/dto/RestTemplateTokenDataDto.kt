package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenDataDto(
    var componentId: String = "",
    var value: String = ""
) : Serializable
