package co.brainz.itsm.boardAdmin.dto

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity

data class BoardCategoryDetailDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?,
    var boardCount: Long? = 0L
)
