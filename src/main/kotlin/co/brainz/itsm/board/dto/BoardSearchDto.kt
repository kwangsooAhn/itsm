package co.brainz.itsm.board.dto

import java.io.Serializable

data class BoardSearchDto(
    var boardAdminId: String = "",
    var search: String = "",
    var fromDt: String = "",
    var toDt: String = ""
) : Serializable
