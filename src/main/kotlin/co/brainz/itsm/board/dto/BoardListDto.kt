/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import java.time.LocalDateTime

data class BoardListDto(
    var boardId: String = "",
    var boardAdminId: String = "",
    var boardCategoryName: String? = "",
    var boardSeq: Long? = 0,
    var boardGroupId: Long? = 0,
    var boardLevelId: Long? = 0,
    var boardTitle: String = "",
    var replyCount: Long = 0,
    var readCount: Long = 0,
    var totalCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = ""
)
