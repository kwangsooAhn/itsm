/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.dto

import java.io.Serializable

data class BoardAdminSearchDto(
    var search: String = "",
    var offset: Long = 0
) : Serializable
