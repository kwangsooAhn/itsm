/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import java.io.Serializable

data class BoardSearchDto(
    var search: String = "",
    var offset: Long = 0,
    var isScroll: Boolean = false
) : Serializable
