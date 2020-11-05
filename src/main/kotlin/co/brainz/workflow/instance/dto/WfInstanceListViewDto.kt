package co.brainz.workflow.instance.dto

import java.io.Serializable

data class WfInstanceListViewDto(
    val tokenEntity: WfInstanceListTokenDto,
    val documentEntity: WfInstanceListDocumentDto,
    val instanceEntity: WfInstanceListInstanceDto
) : Serializable
