/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import java.io.Serializable

data class BoardArticleSearchDto(
    var boardAdminId: String = "",
    var search: String = "",
    var fromDt: String = "",
    var toDt: String = "",
    var offset: Long = 0
) : Serializable