package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.time.LocalDateTime

data class BoardDto(
        var boardId: String = "",
        var boardAdminId: String = "",
        var boardCategoryId: String? = "",
        var boardCategoryName: String? = "",
        var boardSeq: Long? = 0,
        var boardGroupNo: Long? = 0,
        var boardLevelNo: Long? = 0,
        var boardOrderSeq: Long? = 0,
        var boardTitle: String? = "",
        var boardConents: String? = "",
        var replyCount: Long? = 0,
        var readCount: Long? = 0,
        var fileSeqList: List<Long>? = null,
        var createDt: LocalDateTime? = null,
        var createUser: AliceUserEntity? = null,
        var updateDt: LocalDateTime? = null,
        var updateUser: AliceUserEntity? = null
)