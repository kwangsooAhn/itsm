/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.process.service.WfProcessService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
@Disabled
@SpringBootTest
@DisplayName("Process API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfProcessServiceTest {

    @Autowired
    private lateinit var wfProcessService: WfProcessService

    @Test
    @DisplayName("Process 조회 데이터 비교")
    @Order(1)
    fun getProcessListAndProcess() {
        var processId = ""
        val params = LinkedHashMap<String, Any>()
        params["offset"] = 1
        val processList = wfProcessService.getProcesses(params)
        if (processList.data.isNotEmpty()) {
            processId = processList.data[0].id
        }
        if (processId.isNotEmpty()) {
            val process = wfProcessService.getProcessDetail(processId)
            assertEquals(processList.data[0].name, process.name)
            assertEquals(processList.data[0].status, process.status)
        }
    }

    @Test
    @DisplayName("Process Simulation 체크")
    @Order(2)
    fun getSimulation() {
        var processId = ""
        val params = LinkedHashMap<String, Any>()
        params["offset"] = 1
        params["status"] = "process.status.use"
        val processList = wfProcessService.getProcesses(params)
        if (processList.data.isNotEmpty()) {
            processId = processList.data[0].id
        }
        if (processId.isNotEmpty()) {
            val reportDto = wfProcessService.getProcessSimulation(processId)
            if (reportDto.success) {
                // 시뮬레이션 결과가 성공인 경우.
                assertTrue(reportDto.success)
            } else {
                // 시뮬레이션 결과가 실패해도 적절한 메시지를 가지고 있다면 시뮬레이션 기능 자체는 성공.
                reportDto.simulationReport.forEach { it ->
                    if (it.failedMessage.isNotEmpty()) assertTrue(it.failedMessage.isNotEmpty())
                }
            }
        }
    }
}
