/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIDynamicListDto(
    var columnName: ArrayList<String>,
    var columnTitle: ArrayList<String>,
    var columnWidth: ArrayList<String>,
    var columnType: ArrayList<String>,
    val contents: Any?
) : Serializable
