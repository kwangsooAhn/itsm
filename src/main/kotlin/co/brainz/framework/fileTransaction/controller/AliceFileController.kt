/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.controller

import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.dto.AliceFileOwnMapDto
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import co.brainz.framework.fileTransaction.mapper.AliceFileMapper
import co.brainz.framework.fileTransaction.service.AliceFileService
import org.mapstruct.factory.Mappers
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * 파일 관련 컨트롤 클래스
 */
@RestController
class AliceFileController(private val aliceFileService: AliceFileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val fileMapper: AliceFileMapper = Mappers.getMapper(AliceFileMapper::class.java)

    /**
     * 파일 업로드.
     * 파일 추가시 임시폴더에 물리적으로 저장한다.
     */
    @PostMapping("/fileupload")
    fun uploadFile(
        @RequestPart("file") multipartFile: MultipartFile,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        val response: ResponseEntity<Map<String, Any>>
        val map: MutableMap<String, Any> = mutableMapOf()

        when (request.getParameter("target") ?: null) {
            AliceUserConstants.AVATAR_ID -> {
                val fileName = request.getParameter("fileName") ?: null
                map["file"] = aliceFileService.uploadTempAvatarFile(
                    multipartFile,
                    fileName
                )
            }
            AliceUserConstants.PROCESS_ID -> map["file"] = aliceFileService.uploadProcessFile(multipartFile)
            null -> map["file"] = aliceFileService.uploadTemp(multipartFile)
        }

        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json; charset=utf-8")
        response = ResponseEntity(map, headers, HttpStatus.OK)

        return response
    }

    /**
     * 파일 다운로드.
     * 저장된 파일을 다운로드 한다.
     */
    @GetMapping("/filedownload")
    fun downloadFile(@RequestParam seq: Long): ResponseEntity<InputStreamResource> {
        return aliceFileService.download(seq)
    }

    /**
     * 파일 목록 가져오기.
     *
     * @param ownId 파일 목록을 가져오기 위한 값.
     * @param fileDataId 문자열로 파일 목록을 가져오기 위한 값. ex) 111,22,33
     */
    @GetMapping("/filelist")
    fun getFileList(@RequestParam ownId: String, @RequestParam fileDataId: String): List<AliceFileOwnMapDto> {
        return aliceFileService.getList(ownId, fileDataId)
    }

    /**
     * 파일 허용 확장자 목록가져오기
     */
    @GetMapping("/rest/fileNameExtensionList")
    fun getFileNameExtension(): List<AliceFileNameExtensionDto> {
        val fileNameExtensions = mutableListOf<AliceFileNameExtensionDto>()
        val foundFileNameExtensions = aliceFileService.getFileNameExtension()
        for (foundFileNameExtension in foundFileNameExtensions) {
            fileNameExtensions.add(fileMapper.toAliceFileNameExtensionDto(foundFileNameExtension))
        }
        return fileNameExtensions
    }

    /**
     * 파일 삭제.
     *
     * @param seq 파일 고유시퀀스번호
     */
    @DeleteMapping("/filedel")
    fun delete(@RequestParam seq: Long): Boolean {
        aliceFileService.delete(seq)
        return true
    }
}
