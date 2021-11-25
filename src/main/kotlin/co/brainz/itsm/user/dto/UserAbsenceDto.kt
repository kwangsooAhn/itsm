/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable
import java.time.LocalDateTime

data class UserAbsenceDto(
    var startDt: LocalDateTime? = null,
    var endDt: LocalDateTime? = null,
    var substituteUserKey: String? = "",
    var substituteUser: String? = ""
) : Serializable
