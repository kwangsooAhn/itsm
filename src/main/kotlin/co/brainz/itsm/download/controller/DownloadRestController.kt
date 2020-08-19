/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.controller

import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.dto.DownloadSearchDto
import co.brainz.itsm.download.service.DownloadService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/downloads")
class DownloadRestController(private val downloadService: DownloadService) {

    /**
     * [DownloadSearchDto]를 받아서 자료실 추가할 데이터를 반환 [List<DownloadDto>]
     *
     */
    @GetMapping("")
    fun getBoardList(downloadSearchDto: DownloadSearchDto): List<DownloadDto> {
        return downloadService.getDownloadList(downloadSearchDto)
    }

    /**
     * 자료실 신규 등록.
     *
     * @param downloadDto
     */
    @PostMapping("")
    fun createDownload(@RequestBody downloadDto: DownloadDto) {
        downloadService.saveDownload(downloadDto)
    }

    /**
     * 자료실 수정.
     *
     * @param downloadDto
     */
    @PutMapping("")
    fun updateDownload(@RequestBody downloadDto: DownloadDto) {
        downloadService.saveDownload(downloadDto)
    }

    /**
     * 자료실 삭제.
     *
     * @param downloadId
     */
    @DeleteMapping("/{downloadId}")
    fun deleteDownload(@PathVariable downloadId: String) {
        downloadService.deleteDownload(downloadId)
    }
}
