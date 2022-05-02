/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.provider.constants.WorkflowConstants
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/processes")
class ProcessesRestController(
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
    ): ResponseEntity<ZResponse> {
        return when (saveType) {
            WorkflowConstants.ProcessSaveType.SAVE_AS.code -> {
                ZAliceResponse.response(processService.saveAsProcess(jsonData))
            }
            else -> ZAliceResponse.response(processService.createProcess(jsonData))
        }
    }
}
