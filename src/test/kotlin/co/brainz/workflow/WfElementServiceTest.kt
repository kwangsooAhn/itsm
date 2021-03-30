/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.process.service.WfProcessService
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("Element API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfElementServiceTest {

    @Autowired
    private lateinit var wfElementService: WfElementService

    @Autowired
    private lateinit var wfProcessService: WfProcessService

    @Test
    @DisplayName("Process의 시작 Element 조회")
    @Order(1)
    fun getProcessStartElement() {
        var processId = ""
        val params = LinkedHashMap<String, Any>()
        params["offset"] = 1
        val processList = wfProcessService.selectProcessList(params)
        if (processList.isNotEmpty()) {
            processId = processList[0].id
        }
        if (processId.isNotEmpty()) {
            val elementEntity = wfElementService.getStartElement(processId)
            assumeTrue(elementEntity.elementId.isNotEmpty())
        }
    }
}