package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceViewerListDto(
    var instanceId: String,
    var viewerKey: String,
    var viewerName: String = "",
    var organizationName: String? = "",
    var avatarPath: String? = "",
    var reviewYn: Boolean = false,
    var displayYn: Boolean = false,
    var createUserKey: String?,
    var createDt: LocalDateTime?,
    var updateUserKey: String?,
    var updateDt: LocalDateTime?
):Serializable

