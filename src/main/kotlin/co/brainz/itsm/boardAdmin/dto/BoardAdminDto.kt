package co.brainz.itsm.boardAdmin.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.time.LocalDateTime

data class BoardAdminDto(
    var boardAdminId: String = "",
    var boardAdminTitle: String? = "",
    var boardAdminDesc: String? = "",
    var boardAdminSort: Int?,
    var boardUseYn: Boolean = false,
    var answerYn: Boolean = false,
    var commentYn: Boolean = false,
    var categoryYn: Boolean = false,
    var attachYn: Boolean = false,
    var attachFileSize: Long? = 0,
    var boardBoardCount: Long? = 0,
    var enabled: Boolean? = null,
    var createDt: LocalDateTime? = null,
    var createUser: AliceUserEntity? = null,
    var updateDt: LocalDateTime? = null,
    var updateUser: AliceUserEntity? = null
)