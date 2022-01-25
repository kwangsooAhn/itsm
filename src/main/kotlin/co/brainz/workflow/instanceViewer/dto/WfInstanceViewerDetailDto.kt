package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceViewerDetailDto(
    var instanceId: String,
    var viewerKey: String,
    var reviewYn: Boolean = false,
    var displayYn: Boolean = false,
    var createUserKey: String?,
    var createDt: LocalDateTime?,
    var updateUserKey: String?,
    var updateDt: LocalDateTime?
):Serializable
