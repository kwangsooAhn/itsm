/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.dto

import java.io.Serializable

data class DownloadSearchDto(
    var category: String = "",
    var search: String = "",
    var fromDt: String = "",
    var toDt: String = "",
    var offset: Long = 0
) : Serializable
