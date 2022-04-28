/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.file.controller

import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.file.dto.FileRenameDto
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
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
    fun uploadFile(
        @RequestPart("files") multipartFiles: List<MultipartFile>
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(fileService.uploadFiles(multipartFiles))
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{name}")
    fun deleteFile(@PathVariable name: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(fileService.deleteFile(name))
    }

    /**
     * 파일명 수정
     */
    @PutMapping("")
    fun renameFile(@RequestBody fileRenameDto: FileRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            fileService.renameFile(fileRenameDto.originName, fileRenameDto.modifyName)
        )
    }

    /**
     * 파일 조회
     */
    @GetMapping("/{name}")
    fun getFile(@PathVariable name: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(fileService.getFile(name))
    }

    /**
     * 파일 전체 목록 가져오기.
     */
    @GetMapping("")
    fun getFileList(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        @RequestParam(value = "offset", defaultValue = "-1") offset: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            fileProvider.getExternalFileList(type, searchValue, offset.toInt())
        )
    }

    @GetMapping("/download")
    fun download(
        @RequestParam(value = "fileName", defaultValue = "") fileName: String
    ): ResponseEntity<InputStreamResource> {
        return fileService.download(fileName)
    }
}
