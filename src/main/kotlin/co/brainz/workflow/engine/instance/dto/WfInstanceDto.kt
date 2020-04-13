package co.brainz.workflow.engine.instance.dto

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import java.io.Serializable

data class WfInstanceDto(
        val instanceId: String,
        val document: WfDocumentEntity,
        val instanceStatus: String? = null,
        val callTokenId: String? = null
) : Serializable
