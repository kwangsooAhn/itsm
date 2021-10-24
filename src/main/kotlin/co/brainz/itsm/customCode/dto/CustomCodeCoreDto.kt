/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import co.brainz.itsm.customCode.constants.CustomCodeConstants

data class CustomCodeCoreDto(
    var customCodeId: String = "",
    var customCodeName: String? = null,
    var type: String = CustomCodeConstants.Type.TABLE.code,
    var targetTable: String? = null,
    var searchColumn: String? = null,
    var valueColumn: String? = null,
    var pCode: String? = null,
    var condition: String? = null,
    var sessionKey: String? = null
)
