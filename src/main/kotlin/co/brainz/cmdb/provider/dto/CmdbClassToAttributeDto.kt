/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CmdbClassToAttributeDto(
    val attributeId: String? = null,
    val attributeName: String? = null,
    val order: Int? = 1
) : Serializable
