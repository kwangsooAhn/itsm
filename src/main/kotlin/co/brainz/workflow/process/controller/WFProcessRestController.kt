package co.brainz.workflow.process.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonMainDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/wf/processes")
class WFProcessRestController(private val wfEngine: WFEngine) {

    /**
     * 프로세스 목록 조회.
     */
    @GetMapping("")
    fun getProcesses(request: HttpServletRequest): MutableList<WfJsonProcessDto> {
        return wfEngine.process().selectProcessList(request.getParameter("search") ?: "")
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     * @param processDto ProcessDto
     * @return String new process key
     */
    @PostMapping("")
    fun insertProcess(@RequestBody processDto: ProcessDto): ProcessDto {
        return wfEngine.process().insertProcess(processDto)
    }

    /**
     * 프로세스 단건 조회.
     */
    @GetMapping("/{processId}")
    fun getProcess(@PathVariable processId: String): WfJsonMainDto {
        return wfEngine.process().getProcess(processId)
    }

    /**
     * 프로세스 1건 데이터 수정.
     * @param wfJsonMainDto
     * @return Boolean result
     */
    @PutMapping("/{processId}")
    fun updateProcess(@RequestBody wfJsonMainDto: WfJsonMainDto): Boolean {
        return wfEngine.process().updateProcess(wfJsonMainDto)
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

}
