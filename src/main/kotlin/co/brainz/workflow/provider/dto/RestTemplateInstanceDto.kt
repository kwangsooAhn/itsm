package co.brainz.workflow.provider.dto

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import java.io.Serializable

class RestTemplateInstanceDto(
    val instanceId: String,
    val document: WfDocumentEntity,
    val instanceStatus: String? = null,
    val pTokenId: String? = null,
    val documentNo: String? = null
) : Serializable
