/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.archive.dto.ArchiveDto
import co.brainz.itsm.archive.service.ArchiveService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/archives")
class ArchiveRestController(private val archiveService: ArchiveService) {

    /**
     * 자료실 신규 등록.
     *
     * @param archiveDto
     */
    @PostMapping("")
    fun createArchive(@RequestBody archiveDto: ArchiveDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(archiveService.saveArchive(archiveDto))
    }

    /**
     * 자료실 수정.
     *
     * @param archiveDto
     */
    @PutMapping("")
    fun updateArchive(@RequestBody archiveDto: ArchiveDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(archiveService.saveArchive(archiveDto))
    }

    /**
     * 자료실 삭제.
     *
     * @param archiveId
     */
    @DeleteMapping("/{archiveId}")
    fun deleteArchive(@PathVariable archiveId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(archiveService.deleteArchive(archiveId))
    }
}
