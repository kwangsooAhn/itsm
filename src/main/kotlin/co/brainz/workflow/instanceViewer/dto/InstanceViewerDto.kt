package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable
import java.time.LocalDateTime
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity

data class InstanceViewerDto(
    var instanceId: WfInstanceEntity,
    var viewerKey: AliceUserEntity,
    var reviewYn: Boolean? = false,
    var displayYn: Boolean? = false,
    var createUserKey: String? = "",
    var createDt: LocalDateTime?,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime?
): Serializable
