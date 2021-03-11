/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CIRelationDto(
    val relationId: String? = null,
    val relationType: String? = null,
    val masterCIId: String,
    val slaveCIId: String
) : Serializable
