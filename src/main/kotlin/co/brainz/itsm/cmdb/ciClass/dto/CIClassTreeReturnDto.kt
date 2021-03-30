/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.dto

import co.brainz.cmdb.dto.CIClassTreeListDto
import java.io.Serializable

data class CIClassTreeReturnDto(
    val data: List<CIClassTreeListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
