package co.brainz.itsm.process.dto

data class ProcessStatusDto(
    val processId: String,
    val tokenId: String,
    val elementId: String,
    var imageData: String?,
    var left: String?,
    var top: String?,
    var width: String?,
    var height: String?,
    var elements: MutableList<LinkedHashMap<String, String>>?
)


