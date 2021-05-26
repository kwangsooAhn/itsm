/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.dto.AliceFileOwnMapDto
import co.brainz.framework.fileTransaction.mapper.AliceFileMapper
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.dto.PortalTopDto
import co.brainz.itsm.portal.service.PortalService
import org.mapstruct.factory.Mappers
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/portals")
class PortalRestController(
    private val aliceFileService: AliceFileService,
    private val aliceFileProvider: AliceFileProvider,
    private val portalService: PortalService
) {
    private val fileMapper: AliceFileMapper = Mappers.getMapper(AliceFileMapper::class.java)

    /**
     * 포탈 검색 리스트 호출 처리
     */
    @GetMapping("")
    fun getPortalList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        val portalResult = portalService.findPortalListOrSearchList(portalSearchDto)
        val portalResultList = mutableListOf<PortalDto>()
        for (data in portalResult.data) {
            val portalDto = PortalDto(
                portalId = data.portalId,
                portalTitle = data.portalTitle,
                portalContent = data.portalContent,
                createDt = data.createDt,
                updateDt = data.updateDt,
                tableName = data.tableName
            )
            portalResultList.add(portalDto)
        }
        return portalResultList
    }

    @GetMapping("/top")
    fun getTopList(@RequestParam(value = "limit") limit: Long): LinkedHashMap<String, List<PortalTopDto>> {
        return portalService.getTopList(limit)
    }

    /**
     * 파일 허용 확장자 목록가져오기
     */
    @GetMapping("/filenameextensions")
    fun getFileNameExtension(): List<AliceFileNameExtensionDto> {
        val fileNameExtensions = mutableListOf<AliceFileNameExtensionDto>()
        val foundFileNameExtensions = aliceFileProvider.getFileNameExtension()
        for (foundFileNameExtension in foundFileNameExtensions) {
            fileNameExtensions.add(fileMapper.toAliceFileNameExtensionDto(foundFileNameExtension))
        }
        return fileNameExtensions
    }

    /**
     * 포탈 파일 목록 가져오기.
     *
     * @param ownId 파일 목록을 가져오기 위한 값.
     * @param fileDataId 문자열로 파일 목록을 가져오기 위한 값. ex) 111,22,33
     */
    @GetMapping("/files")
    fun getFileList(@RequestParam ownId: String, @RequestParam fileDataId: String): List<AliceFileOwnMapDto> {
        return aliceFileService.getList(ownId, fileDataId)
    }

    /**
     * 파일 다운로드.
     * 저장된 파일을 다운로드 한다.
     */
    @GetMapping("/filedownload")
    fun downloadFile(@RequestParam seq: Long): ResponseEntity<InputStreamResource> {
        return aliceFileService.download(seq)
    }
}
