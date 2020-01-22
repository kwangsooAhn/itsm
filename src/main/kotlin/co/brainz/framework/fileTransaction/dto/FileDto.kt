package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class FileDto(
    var ownId: String,
    var fileSeq: List<Long>?
) : Serializable
