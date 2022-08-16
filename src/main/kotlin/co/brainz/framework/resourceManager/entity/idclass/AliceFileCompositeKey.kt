package co.brainz.framework.resourceManager.entity.idclass

import java.io.Serializable

data class AliceFileCompositeKey(
    var ownId: String = "",
    var fileLocEntity: Long = 0L
) : Serializable
