/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.file.controller

import co.brainz.framework.fileTransaction.dto.AliceImageFileDto
import co.brainz.framework.fileTransaction.dto.AliceImageFileListReturnDto
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.file.dto.FileRenameDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/rest/files")
class FileRestController(
    private val fileService: AliceFileService,
    private val fileProvider: AliceFileProvider
) {

    /**
     * 파일 업로드.
     */
    @PostMapping("")
    fun uploadFile(@RequestPart("files") multipartFiles: List<MultipartFile>): List<AliceImageFileDto> {
        return fileService.uploadImageFiles(multipartFiles)
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{name}")
    fun deleteFile(@PathVariable name: String): Boolean {
        return fileService.deleteImage(name)
    }

    /**
     * 파일명 수정
     */
    @PutMapping("")
    fun renameFile(@RequestBody fileRenameDto: FileRenameDto): Boolean {
        return fileService.renameImage(fileRenameDto.originName, fileRenameDto.modifyName)
    }

    /**
     * 파일 조회
     */
    @GetMapping("/{name}")
    fun getFile(@PathVariable name: String): AliceImageFileDto? {
        return fileService.getImageFile(name)
    }

    /**
     * 파일 전체 목록 가져오기.
     */
    @GetMapping("")
    fun getFileFileList(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        @RequestParam(value = "offset", defaultValue = "-1") offset: String
    ): AliceImageFileListReturnDto {
        return fileProvider.getImageFileList(type, searchValue, offset.toInt())
    }
}
