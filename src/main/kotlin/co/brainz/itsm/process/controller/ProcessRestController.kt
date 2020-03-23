package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.engine.process.dto.WfProcessElementDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
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
class ProcessRestController(private val processService: ProcessService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 불러오기.
     */
    @GetMapping("/data/{processId}")
    fun getProcessData(@PathVariable processId: String): String {
        val processData = processService.getProcess(processId)
        logger.debug("get process data. {}", processData)
        return processData
    }

    /**
     * 프로세스 신규 등록 or 다른 이름 저장.
     */
    @PostMapping("")
    fun createProcess(@RequestParam(value = "saveType", defaultValue = "") saveType: String,
                      @RequestBody jsonData: Any): String {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            RestTemplateConstants.ProcessSaveType.SAVE_AS.code -> processService.saveAsProcess(mapper.convertValue(jsonData, WfProcessElementDto::class.java))
            else -> processService.createProcess(mapper.convertValue(jsonData, RestTemplateProcessDto::class.java))
        }
    }

    /**
     * 프로세스 업데이트.
     */
    @PutMapping("/{processId}")
    fun updateProcess(@RequestBody wfProcessElementDto: WfProcessElementDto): Boolean {
        return processService.updateProcess(wfProcessElementDto)
    }

    /**
     * 프로세스 다른 이름 저장.
     */
    @PostMapping("/data")
    fun saveAsProcess(@RequestBody wfProcessElementDto: WfProcessElementDto): String {
        return processService.saveAsProcess(wfProcessElementDto)
    }

    /**
     * 프로세스 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteForm(@PathVariable processId: String): Boolean {
        return processService.deleteProcess(processId)
    }

}
