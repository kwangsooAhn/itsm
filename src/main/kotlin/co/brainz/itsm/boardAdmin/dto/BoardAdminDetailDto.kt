/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.dto

import java.time.LocalDateTime

data class BoardAdminDetailDto(
    var boardAdminId: String = "",
    var boardAdminTitle: String? = "",
    var boardAdminDesc: String? = "",
    var boardAdminSort: Int?,
    var boardUseYn: Boolean = false,
    var replyYn: Boolean = false,
    var commentYn: Boolean = false,
    var categoryYn: Boolean = false,
    var attachYn: Boolean = false,
    var attachFileSize: Long? = 0,
    var boardBoardCount: Long = 0,
    var enabled: Boolean? = null,
    var categoryInfo: List<BoardCategoryDetailDto>? = null,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = "",
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = ""
)
