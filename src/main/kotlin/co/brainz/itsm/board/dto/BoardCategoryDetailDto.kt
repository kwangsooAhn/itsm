/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

data class BoardCategoryDetailDto(
    var boardCategoryId: String = "",
    var boardAdminId: String = "",
    var boardCategoryName: String? = "",
    var boardCategorySort: Int?,
    var boardCount: Long? = 0L
)
