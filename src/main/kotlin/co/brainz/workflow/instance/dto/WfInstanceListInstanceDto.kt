package co.brainz.workflow.instance.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceListInstanceDto(
    val instanceId: String,
    var instanceStatus: String,
    val instanceStartDt: LocalDateTime? = null,
    var instanceEndDt: LocalDateTime? = null,
    var instanceCreateUser: AliceUserEntity? = null,
    var pTokenId: String? = null,
    val document: WfDocumentEntity,
    var documentNo: String? = null
) : Serializable
