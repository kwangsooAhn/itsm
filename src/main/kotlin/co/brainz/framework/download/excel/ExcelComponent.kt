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
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
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

        // Excel 디자인 적용
        val headerCellStyle = setDefaultCellStyle(workbook, true)
        val bodyCellStyle = setDefaultCellStyle(workbook, false)

        excelVO.sheets.forEachIndexed { sheetsIndex, sheetVO ->
            val sheet = workbook.createSheet(sheetVO.sheetName ?: "Sheet${sheetsIndex + 1}")
            sheetVO.rows.forEachIndexed { rowsIndex, rowsVO ->
                val row = sheet.createRow(rowsIndex)
                rowsVO.cells?.forEachIndexed { index, cellVO ->
                    val cell = row.createCell(index)
                    if (rowsIndex == 0) {
                        cell.cellStyle = headerCellStyle
                        sheet.setColumnWidth(index, cellVO.cellWidth)
                    } else {
                        cell.cellStyle = bodyCellStyle
                    }
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

    fun setDefaultCellStyle(workbook: Workbook, isHeader: Boolean): CellStyle {
        val cellStyle = workbook.createCellStyle()

        if (isHeader) {
            val font = workbook.createFont()
            font.bold = true
            font.fontName = "맑은 고딕"
            font.color = IndexedColors.WHITE.index
            cellStyle.setFont(font)
            cellStyle.fillForegroundColor = IndexedColors.GREY_50_PERCENT.index
            cellStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
            cellStyle.alignment = HorizontalAlignment.CENTER
        }

        cellStyle.borderRight = BorderStyle.THIN
        cellStyle.borderLeft = BorderStyle.THIN
        cellStyle.borderTop = BorderStyle.THIN
        cellStyle.borderBottom = BorderStyle.THIN

        return cellStyle
    }
}
