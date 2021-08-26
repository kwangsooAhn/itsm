/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIRelationDto(
    val relationId: String? = null,
    val relationType: String? = null,
    val sourceCIId: String,
    val targetCIId: String
) : Serializable
