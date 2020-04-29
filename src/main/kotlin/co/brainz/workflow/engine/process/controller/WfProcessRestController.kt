package co.brainz.workflow.engine.process.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.dto.ProcessDto
import co.brainz.workflow.engine.process.dto.WfProcessDto
import co.brainz.workflow.engine.process.dto.WfProcessElementDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
@RequestMapping("/rest/wf/processes")
class WfProcessRestController(private val wfEngine: WfEngine) {

    /**
     * 프로세스 목록 조회.
     */
    @GetMapping("")
    fun getProcesses(@RequestParam parameters: LinkedHashMap<String, Any>): MutableList<WfProcessDto> {
        return wfEngine.process().selectProcessList(parameters)
    }

    /**
     * 프로세스 신규 기본 정보 등록 or 다른 이름 저장.
     * @param saveType String
     * @param jsonData Any
     * @return String new process key
     */
    @PostMapping("")
    fun insertProcess(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): ProcessDto {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            WfProcessConstants.SaveType.SAVE_AS.code -> wfEngine.process()
                .saveAsProcess(mapper.convertValue(jsonData, WfProcessElementDto::class.java))
            else -> wfEngine.process().insertProcess(mapper.convertValue(jsonData, ProcessDto::class.java))
        }
    }

    /**
     * 프로세스 단건 조회.
     */
    @GetMapping("/{processId}")
    fun getProcess(@PathVariable processId: String): WfProcessDto {
        return wfEngine.process().getProcess(processId)
    }

    /**
     * 프로세스 데이터 조회.
     */
    @GetMapping("/{processId}/data")
    fun getProcessData(@PathVariable processId: String): WfProcessElementDto {
        return wfEngine.process().getProcessData(processId)
    }

    /**
     * 프로세스 1건 수정.
     */
    @PutMapping("/{processId}")
    fun updateProcess(@PathVariable processId: String, @RequestBody wfProcessDto: WfProcessDto): Boolean {
        return wfEngine.process().updateProcess(wfProcessDto)
    }

    /**
     * 프로세스 1건 데이터 수정.
     * @param wfProcessElementDto
     * @return Boolean result
     */
    @PutMapping("/{processId}/data")
    fun updateProcessData(
        @PathVariable processId: String,
        @RequestBody wfProcessElementDto: WfProcessElementDto
    ): Boolean {
        return wfEngine.process().updateProcessData(wfProcessElementDto)
    }

    /**
     * 프로세스 1건 데이터 삭제.
     * @param processId
     * @return Boolean result
     */
    @DeleteMapping("/{processId}")
    fun deleteProcess(@PathVariable processId: String): Boolean {
        return wfEngine.process().deleteProcess(processId)
    }

    /**
     * [processId] 프로세스의 디자인이 정상인지 점검한다.
     */
    @GetMapping("/{processId}/simulation")
    fun getProcessSimulation(@PathVariable processId: String): Boolean {
        return wfEngine.process().getProcessSimulation(processId)
    }
}
