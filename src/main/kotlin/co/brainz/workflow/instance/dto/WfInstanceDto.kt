package co.brainz.workflow.instance.dto

import co.brainz.workflow.document.entity.WfDocumentEntity
import java.io.Serializable

data class WfInstanceDto(
        val instanceId: String,
        val document: WfDocumentEntity,
        val instanceStatus: String? = null
) : Serializable
