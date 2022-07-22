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
@RequestMapping("/rest/cmdb/icons")
class CIIconRestController(
    private val ciIconService: CIIconService
) {

    /**
     * 아이콘 파일 전체 목록 가져오기.
     */
    @GetMapping("")
    fun getCIIcons(
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        @RequestParam(value = "offset", defaultValue = "-1") offset: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.getCIIcons(searchValue, offset.toInt())
        )
    }

    /**
     * 파일 업로드.
     */
    @PostMapping("")
    fun uploadCIIcons(
        @RequestPart("files") multipartFiles: List<MultipartFile>
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciIconService.uploadCIIcons(multipartFiles))
    }

    /**
     * 파일명 수정
     */
    @PutMapping("")
    fun renameCIIcon(@RequestBody fileRenameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciIconService.renameCIIcon(fileRenameDto.originPath, fileRenameDto.modifyPath)
        )
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{name}")
    fun deleteCIIcon(@PathVariable name: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciIconService.deleteCIIcon(name))
    }
}
