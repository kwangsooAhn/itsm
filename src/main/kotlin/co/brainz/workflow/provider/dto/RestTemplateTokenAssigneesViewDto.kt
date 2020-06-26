package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenAssigneesViewDto(
    var tokenId: String = "",
    var assigneeType: String = "",
    var assignees: MutableList<String>? = null
) : Serializable