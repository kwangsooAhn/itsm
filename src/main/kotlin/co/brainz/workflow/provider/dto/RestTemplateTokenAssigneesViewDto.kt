package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenAssigneesViewDto(
    var assigneeType: String = "",
    var assignees: MutableList<String>? = null,
    var beforeAssigneeId: String = ""
) : Serializable