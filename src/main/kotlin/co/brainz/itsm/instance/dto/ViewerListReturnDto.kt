/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable

data class ViewerListReturnDto(
    var data: MutableList<ViewerListDto>,
    var totalCount: Long = 0L
) : Serializable
