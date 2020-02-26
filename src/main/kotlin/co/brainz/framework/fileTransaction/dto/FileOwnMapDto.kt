package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class FileOwnMapDto(
        var ownId: String,
        var fileLocDto: FileLocDto
) : Serializable

