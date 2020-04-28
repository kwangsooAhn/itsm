package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import java.time.LocalDateTime

data class BoardViewDto(
    var boardId: String = "",
    var boardAdmin: PortalBoardAdminEntity,
    var boardCategoryId: String? = "",
    var boardCategoryName: String? = "",
    var boardTitle: String? = "",
    var boardContents: String? = "",
    var fileSeqList: List<Long>? = null,
    var createDt: LocalDateTime? = null,
    var createUser: AliceUserEntity? = null,
    var updateDt: LocalDateTime? = null,
    var updateUser: AliceUserEntity? = null
)
