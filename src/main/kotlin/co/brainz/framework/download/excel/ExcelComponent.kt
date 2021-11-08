/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.download.excel

import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.download.excel.impl.ExcelCellComponent
import co.brainz.framework.download.excel.impl.ExcelSheetComponent
import co.brainz.framework.util.AliceUtil
import java.io.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExcelComponent(
    private val excelSheetComponent: ExcelSheetComponent,
    private val excelCellComponent: ExcelCellComponent
) {

    /**
     * Excel 다운로드
     */
    fun download(excelVO: ExcelVO): ResponseEntity<ByteArray> {
        val outputStream = ByteArrayOutputStream()
        val workbook: Workbook = XSSFWorkbook()

        if (excelVO.fileName.isNullOrEmpty()) {
            excelVO.fileName = AliceUtil().getUUID()
        }

        // TODO: CellStyle 이 존재할 경우 추가

        excelVO.sheets.forEachIndexed { sheetsIndex, sheetVO ->
            val sheet = workbook.createSheet(sheetVO.sheetName ?: "Sheet${sheetsIndex + 1}")
            sheetVO.rows.forEachIndexed { rowsIndex, rowsVO ->
                val row = sheet.createRow(rowsIndex)
                rowsVO.cells?.forEachIndexed { index, cellVO ->
                    val cell = row.createCell(index)
                    excelCellComponent.setValue(cell, cellVO.value)
                }
            }
        }

        workbook.write(outputStream)
        outputStream.close()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelVO.fileName)
            .body(outputStream.toByteArray())
    }
}
