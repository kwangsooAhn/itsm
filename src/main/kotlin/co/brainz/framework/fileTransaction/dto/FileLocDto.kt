package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class FileLocDto(
        var fileSeq: Long,
        var fileOwner: String?,
        var uploaded: Boolean?,
        var uploadedLocation: String?,
        var randomName: String?,
        var originName: String?,
        var fileSize: Long?,
        var sort: Int?
) : Serializable
