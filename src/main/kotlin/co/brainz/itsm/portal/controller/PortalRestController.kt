/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/portals")
class PortalRestController(
    private val aliceResourceProvider: AliceResourceProvider,
    private val portalService: PortalService
) {

    /**
     * 포탈 검색 리스트 호출 처리
     */
    @GetMapping("")
    fun getPortalList(portalSearchDto: PortalSearchDto): ResponseEntity<ZResponse> {
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
        return ZAliceResponse.response(portalResultList)
    }

    @GetMapping("/top")
    fun getTopList(@RequestParam(value = "limit") limit: Long): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(portalService.getTopList(limit))
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
     * 파일 허용 확장자 목록가져오기
     */
    @GetMapping("/file/extensions")
    fun getAllowedExtensions(type: String): List<String> {
        return aliceResourceProvider.getAllowedExtensions(type)
    }

    /**
     * 포탈 파일 목록 가져오기.
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
}
