package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessService
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
@RequestMapping("/rest/processes")
class ProcessRestController(private val processService: ProcessService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 불러오기.
     */
    @GetMapping("/{processId}/data")
    fun getProcessData(@PathVariable processId: String): String {
        val processData = processService.getProcessData(processId)
        logger.debug("get process data. {}", processData)
        return processData
    }

    /**
     * 프로세스 업데이트.
     */
    @PutMapping("/{processId}/data")
    fun updateProcess(
        @RequestBody restTemplateProcessElementDto: RestTemplateProcessElementDto,
        @PathVariable processId: String
    ): Boolean {
        return processService.updateProcessData(processId, restTemplateProcessElementDto)
    }

    /**
     * 프로세스 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteForm(@PathVariable processId: String): ResponseEntity<String> {
        return processService.deleteProcess(processId)
    }

    /**
     * 프로세스 시뮬레이션
     */
    @PutMapping("/{processId}/simulation")
    fun getProcessSimulation(
        @RequestBody restTemplateProcessElementDto: RestTemplateProcessElementDto,
        @PathVariable processId: String
    ): String {
        val updated = processService.updateProcessData(processId, restTemplateProcessElementDto)
        return if (updated) {
            processService.getProcessSimulation(restTemplateProcessElementDto.process!!.id)
        } else {
            "false"
        }
    }
}
