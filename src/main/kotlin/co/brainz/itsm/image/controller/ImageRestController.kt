package co.brainz.itsm.image.controller

import co.brainz.framework.fileTransaction.dto.AliceImageFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.image.dto.ImageRenameDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
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
}
