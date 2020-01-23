package co.brainz.itsm.process.controller

import co.brainz.itsm.process.dto.ProcessDto
import co.brainz.itsm.process.dto.ProcessSearchDto
import co.brainz.itsm.process.service.ProcessService
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
@RequestMapping("/rest/processes")
class ProcessRestController(private val processService: ProcessService) {

    /**
     * 프로세스 데이터 조회.
     */
    @GetMapping("")
    fun getProcesses(request: HttpServletRequest): MutableList<ProcessSearchDto> {
        return processService.findProcessList(request.getParameter("search"))
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     */
    @PostMapping("")
    fun insertProcess(@RequestBody processDto: ProcessDto): String {
        return processService.insertProcess(processDto)
    }

    /**
     * 프로세스 1건 데이터 조회.
     */
    @GetMapping("/{processId}")
    fun getProcess(@PathVariable processId: String) {
        //TODO 프로세스 1건 데이터 조회
    }

    /**
     * 프로세스 1건 데이터 수정.
     */
    @PutMapping("/{processId}")
    fun updateProcess(@PathVariable processId: String) {
        //TODO 프로세스 1건 데이터 수정
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteProcess(@PathVariable processId: String) {
        processService.deleteProcess(processId)
    }
}