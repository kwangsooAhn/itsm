/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.controller

import co.brainz.framework.resourceManager.dto.AliceResourceRenameDto
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.cmdb.ciIcon.service.CIIconService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/rest/cmdb/icons")
class CIIconRestController(
    private val ciIconService: CIIconService
) {

    /**
     * 폴더 이름 변경
     */
    @PutMapping("/folder")
    fun renameFolder(
        @RequestBody renameDto: AliceResourceRenameDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.renameFolder(renameDto.originPath, renameDto.modifyPath)
        )
    }

    /**
     * 폴더 삭제
     */
    @DeleteMapping("/folder")
    fun deleteFolder(
        @RequestParam(value = "path", defaultValue = "") path: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.deleteFolder(path)
        )
    }

    /**
     * 파일 업로드.
     */
    @PostMapping("/file/upload")
    fun uploadCIIcons(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "path", defaultValue = "") path: String,
        @RequestPart("files") multipartFiles: List<MultipartFile>
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.uploadCIIcons(type, path, multipartFiles)
        )
    }

    /**
     * 파일명 수정
     */
    @PutMapping("/file")
    fun renameFile(@RequestBody fileRenameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.renameFile(fileRenameDto.originPath, fileRenameDto.modifyPath)
        )
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/file")
    fun deleteCIIcon(
        @RequestParam(value = "path", defaultValue = "") path: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.deleteCIIcon(path)
        )
    }
}
