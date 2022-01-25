package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable

data class WfInstanceViewerDataDto(
    val documentId: String,
    val viewerKey: String,
    val reviewYn: Boolean = false,
    val displayYn: Boolean = false,
    val viewerType: String
) : Serializable
