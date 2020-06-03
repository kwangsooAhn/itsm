package co.brainz.workflow.engine.manager.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable

data class WfTokenDto(
    var tokenId: String = "",
    var documentId: String = "",
    var documentName: String? = null,
    var instanceId: String = "",
    var isAutoComplete: Boolean = false,
    var elementId: String = "",
    var elementType: String = "",
    var tokenStatus: String? = null,
    var assigneeId: String? = null,
    var data: List<WfTokenDataDto>? = null,
    var fileDataIds: String? = null,
    val action: String? = null,
    val numberingId: String? = null,
    var parentTokenId: String? = null,
    var instanceCreateUser: AliceUserEntity? = null
) : Serializable
