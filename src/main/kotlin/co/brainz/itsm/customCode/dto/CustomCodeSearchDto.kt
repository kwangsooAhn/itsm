/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import java.io.Serializable

data class CustomCodeSearchDto(
    var offset: Long = 0,
    var viewType: String?
) : Serializable
