package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable
import java.security.cert.CertPath
import java.time.LocalDateTime
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity

data class ViewerDto(
    var viewerKey: String = "",
    var viewerName: String = "",
    var organizationName: String? ="",
    var avatarPath: String? = "",
    var reviewYn: Boolean? = false,
    var displayYn: Boolean? = false,
    var viewerType: String? = ""
): Serializable
