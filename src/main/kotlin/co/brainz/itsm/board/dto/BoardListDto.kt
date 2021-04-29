/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import java.time.LocalDateTime

data class BoardListDto(
    var boardAdminId: String = "",
    var boardAdminTitle: String? = "",
    var categoryYn: Boolean = false,
    var boardBoardCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = ""
)
