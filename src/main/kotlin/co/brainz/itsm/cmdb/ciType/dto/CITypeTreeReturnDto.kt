/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.dto

import co.brainz.cmdb.dto.CITypeTreeListDto
import java.io.Serializable

data class CITypeTreeReturnDto(
    val data: List<CITypeTreeListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
