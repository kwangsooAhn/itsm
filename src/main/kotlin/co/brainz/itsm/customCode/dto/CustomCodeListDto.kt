/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import java.time.LocalDateTime

data class CustomCodeListDto(
    var customCodeId: String = "",
    var type: String = CustomCodeConstants.Type.TABLE.code,
    var customCodeName: String? = null,
    var sessionKey: String? = null,
    var totalCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null
)
