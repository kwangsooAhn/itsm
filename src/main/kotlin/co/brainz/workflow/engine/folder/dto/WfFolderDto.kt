package co.brainz.workflow.engine.folder.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfFolderDto(
    val instanceId: String?,
    val documentName: String?,
    val instanceStartDt: LocalDateTime?,
    val instanceEndDt: LocalDateTime?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?
) : Serializable {
}
