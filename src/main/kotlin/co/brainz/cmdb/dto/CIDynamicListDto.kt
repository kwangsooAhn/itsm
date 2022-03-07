/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIDynamicListDto(
    var searchItems: MutableList<CISearchItem> = mutableListOf(),
    var columnName: ArrayList<String> = arrayListOf(),
    var columnTitle: ArrayList<String> = arrayListOf(),
    var columnWidth: ArrayList<String> = arrayListOf(),
    var columnType: ArrayList<String> = arrayListOf(),
    var contents: MutableList<CIContentDto> = mutableListOf()
) : Serializable
