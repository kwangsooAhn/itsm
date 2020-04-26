package co.brainz.itsm.board.dto

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import java.time.LocalDateTime

data class BoardSaveDto(
    var boardId: String = "",
    var boardAdminId: String = "",
    var boardAdmin: PortalBoardAdminEntity?,
    var boardCategoryId: String? = "",
    var boardSeq: Long? = 0,
    var boardGroupNo: Long? = 0,
    var boardLevelNo: Long? = 0,
    var boardOrderSeq: Long? = 0,
    var boardTitle: String? = "",
    var boardConents: String? = "",
    var fileSeqList: List<Long>? = null,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null
)
