/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIRelationDto(
    val relationId: String? = null,
    val ciId: String? = null,
    val targetCIId: String,
    val targetCIName: String? = null,
    val targetCIIcon: String? = null,
    var targetCIIconData: String? = null,
    val targetTypeName: String? = null
) : Serializable
