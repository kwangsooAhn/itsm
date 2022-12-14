package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import java.time.LocalDateTime

data class BoardArticleViewDto(
    var boardId: String = "",
    var boardAdmin: PortalBoardAdminEntity,
    var boardCategoryId: String? = "",
    var boardCategoryName: String? = "",
    var boardTitle: String? = "",
    var boardContents: String? = "",
    var createDt: LocalDateTime? = null,
    var createUser: AliceUserEntity? = null
)
