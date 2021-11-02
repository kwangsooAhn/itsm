/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel.dto

import java.io.Serializable

data class ExcelSheetVO(
    val sheetName: String? = null,
    val sheetColor: String? = null,
    val rows: MutableList<ExcelRowVO> = mutableListOf()
) : Serializable
