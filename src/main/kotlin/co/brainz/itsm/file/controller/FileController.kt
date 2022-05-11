/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.file.controller

import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.mapper.AliceFileMapper
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/files")
class FileController(
    private val aliceFileProvider: AliceFileProvider
) {
    private val fileMapper: AliceFileMapper = Mappers.getMapper(AliceFileMapper::class.java)

    private val fileListPage: String = "file/fileList"

    @GetMapping("")
    fun getFiles(model: Model): String {
        val fileNameExtensions = mutableListOf<AliceFileNameExtensionDto>()
        val foundFileNameExtensions = aliceFileProvider.getFileNameExtension()
        for (foundFileNameExtension in foundFileNameExtensions) {
            fileNameExtensions.add(fileMapper.toAliceFileNameExtensionDto(foundFileNameExtension))
        }
        model.addAttribute("acceptFileNameList", fileNameExtensions)
        return fileListPage
    }
}
