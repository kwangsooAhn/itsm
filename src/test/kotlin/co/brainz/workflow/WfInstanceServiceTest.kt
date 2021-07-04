/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.instance.service.WfInstanceService
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumingThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("Instance API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfInstanceServiceTest {

    @Autowired
    private lateinit var wfInstanceService: WfInstanceService

    lateinit var userKey: String
    lateinit var instanceId: String

    @BeforeEach
    fun init() {
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
        this.instanceId = "4028b25d76d23c0d0176d24a3d8c0008"
    }

    @Test
    @DisplayName("Instance 최신 Token 조회")
    @Order(1)
    fun getLatestToken() {
        val tokenDto = wfInstanceService.instance(instanceId)

        val params = LinkedHashMap<String, Any>()
        params["userKey"] = this.userKey
        params["tokenType"] = ""
        params["documentId"] = tokenDto.documentId
        params["searchValue"] = ""
        params["offset"] = 1
        params["fromDt"] = LocalDateTime.now().minusYears(1)
        params["toDt"] = LocalDateTime.now()
        params["tags"] = ""
        val instanceDtoList = wfInstanceService.instances(params)
        for (instanceDto in instanceDtoList.data) {
            instanceDto.tokenId
        }
        val instances = instanceDtoList.data.filter {
            it.documentId == tokenDto.documentId
        }
        assumingThat(
            instances.isNotEmpty()
        ) {
            assertEquals(instances[0].documentId, tokenDto.documentId)
        }
    }
}
