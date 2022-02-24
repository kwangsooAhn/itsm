package co.brainz.workflow.instance.dto

import java.io.Serializable

data class WfInstanceListUserDto(
    val assigneeUserId: String? = null,
    val assigneeUserName: String? = null,
    val timezone: String = "Asia/Seoul"
) : Serializable
