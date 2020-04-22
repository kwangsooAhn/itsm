package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.time.LocalDateTime

data class BoardCommentDto(
        var boardCommentId: String = "",
        var boardId: String = "",
        var boardCommentConents: String = "",
        var createDt: LocalDateTime? = null,
        var createUser: AliceUserEntity? = null,
        var updateDt: LocalDateTime? = null,
        var updateUser: AliceUserEntity? = null
)