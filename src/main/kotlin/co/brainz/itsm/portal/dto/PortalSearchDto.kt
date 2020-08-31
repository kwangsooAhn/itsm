/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.dto

import java.io.Serializable

data class PortalSearchDto(
    val searchValue: String,
    val offset: Long = 0
) : Serializable
