package co.brainz.workflow.provider.dto

import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable

data class RestTemplateWorkflowDto(
    var tokenId: String = "",
    var instance: WfInstanceEntity,
    var element: WfElementEntity,
    var document: WfDocumentEntity,
    var tokenStatus: String? = null,
    var assigneeId: String? = null,
    var data: List<RestTemplateTokenDataDto>? = null,
    var fileDataIds: String? = null,
    val action: String? = null,
    //val numberingId: String? = null,
    var parentTokenId: String? = null
) : Serializable
