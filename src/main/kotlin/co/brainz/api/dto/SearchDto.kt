/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.dto

import java.io.Serializable

data class SearchDto(
    val search: String? = null,
    val offset: Long?,
    val limit: Long?
) : Serializable
