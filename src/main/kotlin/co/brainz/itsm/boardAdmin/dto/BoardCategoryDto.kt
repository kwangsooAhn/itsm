package co.brainz.itsm.boardAdmin.dto

import java.io.Serializable

data class BoardCategoryDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?
): Serializable