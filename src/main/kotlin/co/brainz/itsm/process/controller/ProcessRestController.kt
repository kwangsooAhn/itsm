package co.brainz.itsm.process.controller

import co.brainz.workflow.engine.WFEngine
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/processes")
class ProcessRestController(private val wfEngine: WFEngine) {

    /**
     * 프로세스 불러오기.
     */
    @GetMapping("/data/{processId}")
    fun getProcessData(@PathVariable processId: String): String {
        // 테스트용 프로세스 데이터
        return """
               {"process": {"id": "$processId", "name": "서비스데스크", "description": "서비스데스크입니다."},
                "elements": [{
                              "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a4",
                              "category": "event",
                              "type": "start",
                              "display": {"width": 38, "height": 50, "position-x": 10, "position-y": 10},
                              "data": {"name": "시작"}
                             }]
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
}
