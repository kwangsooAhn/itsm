package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class AliceFileDto(
    var ownId: String,
    var fileSeq: List<Long>?,
    var delFileSeq: List<Long>? = null
) : Serializable
