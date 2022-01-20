package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable

data class WfInstanceViewerDataDto(
    var viewerKey: String = "",
    var viewerName: String = "",
    var organizationName: String? = "",
    var avatarPath: String? = "",
    var reviewYn: Boolean = false,
    var displayYn: Boolean? = false,
    var viewerType: String? = ""
) : Serializable
