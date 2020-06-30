package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateRelatedInstanceDto(
    val folderId: String?,
    val instanceId: String?,
    val relatedType: String?,
    val tokenId: String? = null,
    val documentNo: String?,
    val documentName: String?,
    val createUserKey: String?,
    val createDt: LocalDateTime? = null,
    var instanceStartDt: LocalDateTime?,
    var instanceEndDt: LocalDateTime?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?
) : Serializable
