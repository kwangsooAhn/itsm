/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel.dto

import java.io.Serializable

data class ExcelCellVO(
    val value: Any? = null
) : Serializable
