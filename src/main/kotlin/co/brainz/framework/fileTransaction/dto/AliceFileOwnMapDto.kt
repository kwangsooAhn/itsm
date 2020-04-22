package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class AliceFileOwnMapDto(
    var ownId: String,
    var fileLocDto: AliceFileLocDto
) : Serializable
