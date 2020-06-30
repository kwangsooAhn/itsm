package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenStakeholderViewDto(
    var type: String = "",
    var assignee: MutableList<String>? = null,
    var revokeAssignee: String = ""
) : Serializable