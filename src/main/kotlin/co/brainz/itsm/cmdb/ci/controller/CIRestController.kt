/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIListDto
import co.brainz.itsm.cmdb.ci.dto.CIComponentDataDto
import co.brainz.itsm.cmdb.ci.dto.CISearch
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.cmdb.ci.service.CITemplateService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/rest/cmdb/cis")
class CIRestController(
    private val ciService: CIService,
    private val ciTemplateService: CITemplateService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("")
    fun getCIs(): List<CIListDto> {
        return ciService.getCIList()
    }

    @GetMapping("/{ciId}")
    fun getCI(request: HttpServletRequest, model: Model, @PathVariable ciId: String): CIDetailDto {
        return ciService.getCI(ciId)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 등록
     */
    @PostMapping("/{ciId}/data")
    fun saveCIComponentData(@PathVariable ciId: String, @RequestBody ciComponentData: String): Boolean {
        return ciService.saveCIComponentData(ciId, ciComponentData)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 삭제
     */
    @DeleteMapping("/data")
    fun deleteCIComponentData(request: HttpServletRequest): Boolean {
        return ciService.deleteCIComponentData(
            request.getParameter("ciId"),
            request.getParameter("componentId")
        )
    }

    /**
     * CI 컴포넌트 - CI 컴포넌트 세부 정보 조회
     */
    @GetMapping("/{ciId}/data")
    fun getCIComponentData(request: HttpServletRequest, @PathVariable ciId: String): CIComponentDataDto? {
        return ciService.getCIComponentData(
            ciId,
            request.getParameter("componentId"),
            request.getParameter("instanceId")
        )
    }

    /**
     * CI 연관 관계 데이터 조회
     */
    @GetMapping("/{ciId}/relation")
    fun getCIRelations(request: HttpServletRequest, @PathVariable ciId: String): Any? {
        val parameter = LinkedHashMap<String, String>()
        parameter["ciId"] = ciId
        parameter["componentId"] = request.getParameter("componentId")
        parameter["instanceId"] = request.getParameter("instanceId")
        return ciService.getCIRelations(parameter)
    }

    /**
     * CI 조회 Excel 다운로드
     */
    @PostMapping("/excel")
    fun getCIsExcelDownload(
        ciSearchCondition: CISearchCondition,
        @RequestBody searchItemsData: CISearch
    ): ResponseEntity<ByteArray> {
        return ciService.getCIsExcelDownload(ciSearchCondition, searchItemsData)
    }

    /**
     * CI 일괄 등록 템플릿 다운로드
     */
    @GetMapping("/template")
    fun getCIsTemplateDownload(
        @RequestParam typeId: String
    ): ResponseEntity<ByteArray> {
        return ciTemplateService.getCIsTemplateDownload(typeId)
    }

    /**
     * CI 일괄 등록 템플릿 업로드
     */
    @PostMapping("/templateUpload")
    fun uploadTemplate(
        @RequestPart("files") files: MultipartFile
    ) {
        ciTemplateService.uploadCIsTemplate(files)
    }
}
