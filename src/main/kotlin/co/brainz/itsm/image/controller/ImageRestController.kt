/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.image.controller

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.fileTransaction.dto.AliceImageFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.image.dto.ImageRenameDto
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/rest/images")
class ImageRestController(private val fileService: AliceFileService) {

    /**
     * 이미지 파일 업로드.
     */
    @PostMapping("")
    fun uploadFile(@RequestPart("files") multipartFiles: List<MultipartFile>): List<AliceImageFileDto> {
        return fileService.uploadImageFiles(multipartFiles)
    }

    /**
     * 이미지 삭제
     */
    @DeleteMapping("/{name}")
    fun deleteFile(@PathVariable name: String): Boolean {
        return fileService.deleteImage(name)
    }

    /**
     * 이미지명 수정
     */
    @PutMapping("")
    fun renameFile(@RequestBody imageRenameDto: ImageRenameDto): Boolean {
        return fileService.renameImage(imageRenameDto.originName, imageRenameDto.modifyName)
    }

    /**
     * 이미지 조회
     */
    @GetMapping("/{name}")
    fun getFile(@PathVariable name: String): AliceImageFileDto? {
        return fileService.getImageFile(name)
    }

    /**
     * 이미지 파일 전체 목록 가져오기.
     */
    @GetMapping("")
    fun getImageFileList(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String
    ): List<AliceImageFileDto> {
        return when (type) {
            AliceConstants.FileType.ICON.code, AliceConstants.FileType.ICON_TYPE.code -> fileService.getInternalImageDataList(
                type,
                searchValue
            )
            else -> fileService.getExternalImageDataList(type, searchValue)
        }
    }
}
