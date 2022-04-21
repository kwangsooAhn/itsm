package co.brainz.itsm.process.dto

data class TokenStatusDto(
    val tokenId: String,
    val processId: String,
    val elementId: String,
    var imageData: String?,
    var left: String?,
    var top: String?,
    var width: String?,
    var height: String?,
    var elements: MutableList<LinkedHashMap<String, String>>?
)
