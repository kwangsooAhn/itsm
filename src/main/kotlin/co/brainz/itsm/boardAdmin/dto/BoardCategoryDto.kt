package co.brainz.itsm.boardAdmin.dto

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity

data class BoardCategoryDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardAdmin: PortalBoardAdminEntity?,
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?,
    var boardCount: Long? = 0L
)
