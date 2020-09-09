package co.brainz.itsm.board.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.board.entity.PortalBoardEntity
import java.time.LocalDateTime

data class BoardCommentDto(
    var boardCommentId: String = "",
    var boardId: String = "",
    var commentBoard: PortalBoardEntity?,
    var boardCommentContents: String = "",
    var createDt: LocalDateTime? = null,
    var createUser: AliceUserEntity? = null,
    var updateDt: LocalDateTime? = null,
    var updateUser: AliceUserEntity? = null,
    var avatarPath: String? = null
)
