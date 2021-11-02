/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel.impl

import org.apache.poi.ss.usermodel.Cell
import org.springframework.stereotype.Component

@Component
class ExcelCellComponent {

    fun setValue(cell: Cell, value: Any?) {
        when (value) {
            is String -> cell.setCellValue(value)
            is Int -> cell.setCellValue(value.toDouble())
            is Double -> cell.setCellValue(value)
            is Boolean -> cell.setCellValue(value)
            else -> cell.setCellValue(value.toString())
        }
    }
}
