/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel.dto

import java.io.Serializable

data class ExcelVO(
    var fileName: String? = "",
    val sheets: MutableList<ExcelSheetVO> = mutableListOf(),
    val cellStyles: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
