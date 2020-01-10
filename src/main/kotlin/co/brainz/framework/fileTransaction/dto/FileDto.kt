package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class FileDto(
        var seq: Long?,
        var name: String?,
        var originName: String?,
        var size: Long?,
        var dir: String?,
        var isUpload: Boolean = false,
        var searchKey: String?,
        var type: String?
): Serializable
