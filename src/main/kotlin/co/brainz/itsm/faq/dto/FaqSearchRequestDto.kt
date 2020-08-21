/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.dto

import java.io.Serializable

data class FaqSearchRequestDto(
    var search: String? = null,
    var groupCodes: MutableList<String>? = null,
    var offset: Long = 0
) : Serializable
