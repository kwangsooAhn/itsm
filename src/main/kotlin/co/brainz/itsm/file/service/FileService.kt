/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.file.service

import co.brainz.framework.fileTransaction.dto.AliceFileDetailDto
import co.brainz.framework.fileTransaction.dto.AliceFileDetailListReturnDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.response.dto.ZResponse
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val aliceFileService: AliceFileService
) {
    /**
     * 파일 관리 - 파일 조회
     *
     * @param name
     * @return AliceFileDetailDto?
     */
    fun getFile(name: String): AliceFileDetailDto? {
        return aliceFileService.getFile(name)
    }

    /**
     * 파일 관리 - 다운로드
     *
     * @param fileName
     * @return ResponseEntity<InputStreamResource>
     */
    fun downloadFile(fileName: String): ResponseEntity<InputStreamResource> {
        return aliceFileService.downloadFile(fileName)
    }

    /**
     * 파일 관리 - 파일 업로드
     *
     * @param multipartFiles
     * @return ZResponse
     */
    fun uploadFiles(multipartFiles: List<MultipartFile>): ZResponse {
        return aliceFileService.uploadFiles(multipartFiles)
    }

    /**
     * 파일 관리 - 파일명 수정
     *
     * @param originName
     * @param modifyName
     * @return ZResponse
     */
    fun renameFile(originName: String, modifyName: String): ZResponse {
        return aliceFileService.renameFile(originName, modifyName)
    }

    /**
     * 파일 관리 - 파일 삭제
     *
     * @param name
     * @return ZResponse
     */
    fun deleteFile(name: String): ZResponse {
        return aliceFileService.deleteFile(name)
    }

    /**
     * 파일 관리 - 외부 경로에 있는 파일 정보 조회
     *
     * @param type
     * @param searchValue
     * @param currentOffset
     * @return AliceFileDetailListReturnDto
     */
    fun getExternalFileList(
        type: String,
        searchValue: String,
        currentOffset: Int = -1
    ): AliceFileDetailListReturnDto {
        return aliceFileService.getExternalFileList(type, searchValue, currentOffset)
    }
}
