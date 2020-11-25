package co.brainz.itsm.board.dto

import co.brainz.itsm.board.entity.PortalBoardAdminEntity

data class BoardCategoryDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardAdmin: PortalBoardAdminEntity?,
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?,
    var boardCount: Long? = 0L
)
