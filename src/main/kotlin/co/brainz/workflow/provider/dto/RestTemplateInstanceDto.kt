package co.brainz.workflow.provider.dto

import java.io.Serializable

class RestTemplateInstanceDto(
    val instanceId: String,
    val documentId: String,
    val instanceStatus: String? = null,
    val instanceCreateUser: String? = null,
    val pTokenId: String? = null,
    val documentNo: String? = null
) : Serializable
