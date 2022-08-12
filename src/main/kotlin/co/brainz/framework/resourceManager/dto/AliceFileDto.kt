package co.brainz.framework.resourceManager.dto

import java.io.Serializable

data class AliceFileDto(
    var ownId: String,
    var fileSeq: List<Long>?,
    var delFileSeq: List<Long>? = null
) : Serializable

data class AliceFileLocDto(
    var fileSeq: Long,
    var fileOwner: String?,
    var uploaded: Boolean?,
    var uploadedLocation: String?,
    var randomName: String?,
    var originName: String?,
    var fileSize: Long?,
    var sort: Int?
) : Serializable

data class AliceFileOwnMapDto(
    var ownId: String,
    var fileLocDto: AliceFileLocDto
) : Serializable
