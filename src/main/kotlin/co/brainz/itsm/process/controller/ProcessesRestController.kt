/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessAdminService
import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/processes")
class ProcessesRestController(
    private val processAdminService: ProcessAdminService,
    private val processService: ProcessService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 신규 등록 or 다른 이름 저장.
     */
    @PostMapping("")
    fun createProcess(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): String {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            RestTemplateConstants.ProcessSaveType.SAVE_AS.code -> processService.saveAsProcess(
                mapper.convertValue(
                    jsonData,
                    RestTemplateProcessElementDto::class.java
                )
            )
            else -> processService.createProcess(mapper.convertValue(jsonData, RestTemplateProcessDto::class.java))
        }
    }

    /**
     * 프로세스 리스트 조회 (스크롤).
     */
    @GetMapping("")
    fun getProcessList(request: HttpServletRequest, model: Model): List<RestTemplateProcessViewDto> {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        return processAdminService.getProcesses(params)
    }

    /**
     * 프로세스 수정
     */
    @PutMapping("/{processId}")
    fun updateProcess(
        @RequestBody restTemplateProcessDto: RestTemplateProcessDto,
        @PathVariable processId: String
    ): Boolean {
        return processAdminService.updateProcess(processId, restTemplateProcessDto)
    }

    /**
     * 프로세스 조회
     */
    @GetMapping("/{processId}/data")
    fun getProcess(@PathVariable processId: String): RestTemplateProcessViewDto {
        return processAdminService.getProcessAdmin(processId)
    }

    /**
     * 프로세스 목록 조회.
     */
    @GetMapping("/all")
    fun getProcessList(
        @RequestParam(
            value = "status",
            defaultValue = ""
        ) status: String
    ): List<RestTemplateProcessViewDto> {
        val params = LinkedHashMap<String, Any>()
        params["status"] = status
        return processAdminService.getProcesses(params)
    }
}
