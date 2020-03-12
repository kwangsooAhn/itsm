package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class AliceFileDto(
    var ownId: String,
    var fileSeq: List<Long>?
) : Serializable
