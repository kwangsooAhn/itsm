package co.brainz.workflow.provider.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import java.io.Serializable

class RestTemplateInstanceDto(
    val instanceId: String,
    val document: WfDocumentEntity,
    val instanceStatus: String? = null,
    val instanceCreateUser: AliceUserEntity? = null,
    val pTokenId: String? = null,
    val documentNo: String? = null
) : Serializable
