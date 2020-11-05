package co.brainz.workflow.instance.dto

import java.io.Serializable

data class WfInstanceListTokenDataDto(
    val component: WfInstanceListComponentDto,
    val value: String = ""
) : Serializable
