package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable

data class WfInstanceViewerListReturnDto(
    var data: MutableList<WfInstanceViewerListDto>?,
    var totalCount: Long = 0L
) : Serializable
