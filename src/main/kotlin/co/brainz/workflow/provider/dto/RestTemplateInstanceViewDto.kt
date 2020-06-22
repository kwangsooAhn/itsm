package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceViewDto(
    val tokenId: String,
    val instanceId: String,
    val documentName: String,
    val documentDesc: String? = "",
    val elementName: String? = "",
    val topics: MutableList<String>?,
    var createDt: LocalDateTime?,
    val assigneeUserKey: String? = "",
    val assigneeUserName: String? = "",
    val createUserKey: String? = "",
    val createUserName: String? = "",
    val documentId: String,
    val documentNo: String? = null,
    val documentColor: String?,
    val totalCount: Long
) : Serializable
