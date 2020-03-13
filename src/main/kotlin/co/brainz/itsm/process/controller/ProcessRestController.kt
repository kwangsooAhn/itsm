package co.brainz.itsm.process.controller

import co.brainz.itsm.provider.ProviderProcess
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.workflow.process.dto.WfProcessElementDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/processes")
class ProcessRestController(private val providerProcess: ProviderProcess) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 불러오기.
     */
    @GetMapping("/data/{processId}")
    fun getProcessData(@PathVariable processId: String): String {
        val processData = providerProcess.getProcess(processId)
        logger.debug("get process data. {}", processData)
        return processData
    }

    /**
     * 프로세스 저장.
     */
    @PostMapping("/data")
    fun saveProcessData(@RequestBody processData: String): String {
        // 테스트용 데이터
        println(processData)
        return "1"
    }

    /**
     * 프로세스 신규 등록.
     */
    @PostMapping("")
    fun createProcess(@RequestBody processDto: ProcessDto): String {
        return providerProcess.createProcess(processDto)
    }

    /**
     * 프로세스 업데이트.
     */
    @PutMapping("/{processId}")
    fun updateProcess(@RequestBody wfProcessElementDto: WfProcessElementDto): Boolean {
        return providerProcess.updateProcess(wfProcessElementDto)
    }

    /**
     * 프로세스 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteForm(@PathVariable processId: String): Boolean {
        return providerProcess.deleteProcess(processId)
    }

}
