package co.brainz.workflow.instance.dto

import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.io.Serializable

data class WfInstanceListViewDto(
    val tokenEntity: WfTokenEntity,
    val documentEntity: WfDocumentEntity,
    val instanceEntity: WfInstanceEntity
) : Serializable
