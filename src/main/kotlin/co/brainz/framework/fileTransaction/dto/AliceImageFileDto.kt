package co.brainz.framework.fileTransaction.dto

data class AliceImageFileDto(
    var name: String,
    var fullpath: String,
    var extension: String,
    var size: String,
    var data: String,
    var width: Int,
    var height: Int
)
