package co.brainz.workflow.instance.dto

import co.brainz.workflow.document.entity.DocumentEntity
import java.io.Serializable

data class InstanceDto(
        val instanceId: String,
        val document: DocumentEntity,
        val instanceStatus: String? = null
) : Serializable
