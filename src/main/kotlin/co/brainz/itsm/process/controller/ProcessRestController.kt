/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/process")
class ProcessRestController(private val processService: ProcessService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 불러오기.
     */
    @GetMapping("/{processId}/data")
    fun getProcessData(@PathVariable processId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(processService.getProcessData(processId))
    }

    /**
     * 프로세스 업데이트.
     */
    @PutMapping("/{processId}/data")
    fun updateProcess(
        @RequestBody restTemplateProcessElementDto: RestTemplateProcessElementDto,
        @PathVariable processId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            processService.updateProcessData(processId, restTemplateProcessElementDto)
        )
    }

    /**
     * 프로세스 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteForm(@PathVariable processId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(processService.deleteProcess(processId))
    }

    /**
     * 프로세스 시뮬레이션
     */
    @PutMapping("/{processId}/simulation")
    fun getProcessSimulation(
        @RequestBody restTemplateProcessElementDto: RestTemplateProcessElementDto,
        @PathVariable processId: String
    ): ResponseEntity<ZResponse> {
        var updated = ZResponseConstants.STATUS.SUCCESS.code
        if (restTemplateProcessElementDto.process?.status == WorkflowConstants.ProcessStatus.EDIT.value) {
            updated = processService.updateProcessData(processId, restTemplateProcessElementDto).status
        }
        return if (updated == ZResponseConstants.STATUS.SUCCESS.code) {
            ZAliceResponse.response(processService.getProcessSimulation(restTemplateProcessElementDto.process!!.id))
        } else {
            ZAliceResponse.response(ZResponse(status = ZResponseConstants.STATUS.ERROR_FAIL.code))
        }
    }
}
