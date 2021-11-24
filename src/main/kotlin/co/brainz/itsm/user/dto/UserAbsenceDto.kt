/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserAbsenceDto(
    var absenceStartDt: String? = "",
    var absenceEndDt: String? = "",
    var substituteUserKey: String? = "",
    var assigneeChange: Boolean? = false
) : Serializable
