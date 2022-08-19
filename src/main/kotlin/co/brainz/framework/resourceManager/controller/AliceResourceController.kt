/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.resourceManager.controller

import co.brainz.framework.resourceManager.dto.AliceResourceRenameDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import java.io.File
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * 리소스 파일, 폴더 관련 컨트롤 클래스
 */
@RestController
@RequestMapping("/rest/resources")
class AliceResourceController(
    private val aliceResourceProvider: AliceResourceProvider
) {

    @Value("\${file.drag.enabled}")
    private var enabledFileDrag: Boolean = false

    /**
     * 파일 Drag 사용 여부
     */
    @GetMapping("/enabledFileDrag")
    fun getFileDragEnabled(): Boolean {
        return enabledFileDrag
    }

    /**
     * 기본 경로 조회
     */
    @GetMapping("/basePath")
    fun getBasePath(
        @RequestParam(value = "type", defaultValue = "") type: String
    ): String {
        return aliceResourceProvider.getExternalPath(type).toAbsolutePath().toString()
    }

    /**
     * 파일 구분자
     */
    @GetMapping("/fileSeparator")
    fun getFileSeperator(): String {
        return File.separator
    }

    /**
     * 폴더 추가
     */
    @PostMapping("/folder")
    fun createFolder(@RequestBody renameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.createFolder(renameDto.originPath)
        )
    }

    /**
     * 폴더 이름 변경
     */
    @PutMapping("/folder")
    fun renameFolder(@RequestBody renameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.renameFileAndFolder(renameDto.originPath, renameDto.modifyPath)
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
            aliceResourceProvider.deleteFolder(path)
        )
    }

    /**
     * 파일 조회 (단일).
     */
    @GetMapping("/file")
    fun getFile(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "path", defaultValue = "") path: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.getFile(type, path)
        )
    }

    /**
     * 파일 업로드.
     * 파일 추가시 임시폴더에 물리적으로 저장한다.
     */
    @PostMapping("/file/upload")
    fun uploadFiles(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "path", defaultValue = "") path: String,
        @RequestPart("files") multipartFiles: List<MultipartFile>,
        request: HttpServletRequest
    ): ResponseEntity<ZResponse> {
        val fileName = request.getParameter("fileName") ?: null

        return ZAliceResponse.response(
            aliceResourceProvider.uploadFiles(type, path, multipartFiles, fileName)
        )
    }

    /**
     * 파일 다운로드.
     * 저장된 파일을 다운로드 한다.
     */
    @GetMapping("/file/download")
    fun downloadFile(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "path", defaultValue = "") path: String
    ): ResponseEntity<InputStreamResource> {
        return aliceResourceProvider.downloadFile(type, path)
    }

    /**
     * 파일 삭제.
     */
    @DeleteMapping("/file")
    fun deleteFile(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "path", defaultValue = "") path: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.deleteFile(type, path)
        )
    }

    /**
     * 파일 이름 변경
     */
    @PutMapping("/file")
    fun renameFile(@RequestBody renameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.renameFileAndFolder(renameDto.originPath, renameDto.modifyPath)
        )
    }

    /**
     * 파일 허용 확장자 목록가져오기
     */
    @GetMapping("/file/extensions")
    fun getAllowedExtensions(type: String): List<String> {
        return aliceResourceProvider.getAllowedExtensions(type)
    }

    /**
     * 파일 목록 가져오기.
     *
     * @param ownId 파일 목록을 가져오기 위한 값.
     * @param fileDataId 문자열로 파일 목록을 가져오기 위한 값. ex) 111,22,33
     */
    @GetMapping("/files")
    fun getFileList(@RequestParam ownId: String, @RequestParam fileDataId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.getFiles(ownId, fileDataId)
        )
    }

    /**
     * 파일 이동
     */
    @PutMapping("/file/move")
    fun moveFile(@RequestBody renameDto: AliceResourceRenameDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            aliceResourceProvider.moveFile(renameDto.originPath, renameDto.modifyPath)
        )
    }
}
