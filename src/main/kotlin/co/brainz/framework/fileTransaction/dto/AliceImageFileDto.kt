package co.brainz.framework.fileTransaction.dto

import java.time.LocalDateTime

data class AliceImageFileDto(
    var name: String,
    var fullpath: String,
    var extension: String,
    var size: String,
    var data: String,
    var width: Int,
    var height: Int,
    var updateDt: LocalDateTime?
)
