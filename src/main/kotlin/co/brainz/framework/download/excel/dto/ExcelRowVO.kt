/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel.dto

import java.io.Serializable

data class ExcelRowVO(
    val cells: List<ExcelCellVO>? = null
) : Serializable
