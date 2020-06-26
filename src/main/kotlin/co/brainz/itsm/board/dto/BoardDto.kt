package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.time.LocalDateTime

data class BoardDto(
    var boardId: String = "",
    var boardAdminId: String = "",
    var boardCategoryName: String? = "",
    var boardSeq: Long? = 0,
    var boardGroupId: Long? = 0,
    var boardLevelId: Long? = 0,
    var boardTitle: String = "",
    var replyCount: Long = 0,
    var readCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUser: AliceUserEntity? = null
)
