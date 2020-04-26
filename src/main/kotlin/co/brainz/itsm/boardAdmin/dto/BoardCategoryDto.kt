package co.brainz.itsm.boardAdmin.dto

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import java.io.Serializable

data class BoardCategoryDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardAdmin: PortalBoardAdminEntity?,
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?
) : Serializable
