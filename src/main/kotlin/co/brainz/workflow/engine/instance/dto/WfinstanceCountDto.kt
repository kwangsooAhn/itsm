package co.brainz.workflow.engine.instance.dto

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import java.io.Serializable

data class WfInstanceCountDto(
        val instanceStatus: String,
        val instanceCount: Int? = 0
) : Serializable