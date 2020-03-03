package co.brainz.itsm.process.controller

import co.brainz.itsm.provider.ProviderProcess
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonMainDto
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
//        return processData
        // TODO 2019-03-03 kbh - js가 작업중이라 테스트 데이터를 사용한다. 작업 완료시  return processData로 사용하기
        // 테스트용 프로세스 데이터
        return """
               {"process": {"id": "$processId", "name": "서비스데스크", "description": "서비스데스크입니다.", "xxx":"xxx"},
                "elements": [{
                              "id": "4a417b48be2e4ebe82bf8f80a63622a4",
                              "type": "commonStart",
                              "display": {"width": 38, "height": 50, "position-x": 100, "position-y": 100},
                              "data": {"end1": "시작"}
                             },
                             {
                              "id": "4a417b48be2e4ebe82bf8f80a63622a1",
                              "type": "userTask",
                              "display": {"width": 100, "height": 50, "position-x": 200, "position-y": 100},
                              "data": {"name": "신청서작성"}
                             },
                             {
                              "id": "4a417b48be2e4ebe82bf8f80a63622a2",
                              "type": "arrowConnector",
                              "data": {"name": "승인", "condition": "", "start-id": "4a417b48be2e4ebe82bf8f80a63622a4", "end-id": "4a417b48be2e4ebe82bf8f80a63622a1"}
                             }
                            ]
               }
               """
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
    fun updateProcess(@RequestBody wfJsonMainDto: WfJsonMainDto): Boolean {
        return providerProcess.updateProcess(wfJsonMainDto)
    }

    /**
     * 프로세스 삭제.
     */
    @DeleteMapping("/{processId}")
    fun deleteForm(@PathVariable processId: String): Boolean {
        return providerProcess.deleteProcess(processId)
    }

}
