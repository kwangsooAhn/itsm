package co.brainz.workflow.process

import co.brainz.workflow.engine.WFEngine
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/processes")
class WFProcessRestController(private val wfEngine: WFEngine) {

    /**
     * 프로세스 데이터 조회.
     */
    @GetMapping("")
    fun getProcesses(search: String): MutableList<ProcessDto> {
        return wfEngine.process().selectProcessList(search)
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     */
    @PostMapping("")
    fun insertProcess(@RequestBody processDto: ProcessDto): String {
        return wfEngine.process().insertProcess(processDto)
    }

    /**
     * 프로세스 1건 데이터 조회.
     */
    @GetMapping("/{processId}")
    fun getProcess(@PathVariable processId: String) {
        TODO("Should be implemented")
    }

    /**
     * 프로세스 1건 데이터 수정.
     */
    @PutMapping("/{processId}")
    fun updateProcess(@PathVariable processId: String) {
        TODO("Should be implemented")
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteProcess(@PathVariable processId: String) {
        wfEngine.process().deleteProcess(processId)
    }

}
