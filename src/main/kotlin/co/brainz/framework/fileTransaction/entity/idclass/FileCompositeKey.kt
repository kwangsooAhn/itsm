package co.brainz.framework.fileTransaction.entity.idclass

import java.io.Serializable

data class FileCompositeKey(
    var ownId: String = "",
    var fileLocEntity: Long = 0L
) : Serializable
