/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api

import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.token.constants.WfTokenConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.junit.jupiter.api.Assumptions.assumingThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Call API Document")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CallApiDocument {

    // 1. accessToken를 조회는 기능 구현
    // 2. 단순문의 데이터 구조를 조회하는 기능 구현
    // 3. 단순문의 컴포넌트별 데이터 조회 기능 구현

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var wfInstanceService: WfInstanceService

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private val documentId = "4028b26478d45b5a0178d489c3660004" // 단순문의 신청서 ID
    private val userId = "lizeelf"
    private val password = "itsm123123"
    lateinit var accessToken: String

    @DisplayName("Get AccessToken")
    @BeforeEach
    fun getAccessToken() {
        // Make BodyContent
        val params = LinkedHashMap<String, Any>()
        params["userId"] = this.userId
        params["password"] = this.password
        val bodyContent = mapper.writeValueAsString(params)
        val accessResult = mvc.perform(get("/api/tokens").content(bodyContent))
            .andExpect(status().isOk)
            .andReturn()
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        val resultMap: LinkedHashMap<String, Any> =
            mapper.readValue(accessResult.response.contentAsString, linkedMapType)
        this.accessToken = resultMap["access_token"].toString()
    }

    @DisplayName("문서의 데이터 타입")
    @Order(1)
    @Test
    fun getDocumentDataStructure() {
        mvc.perform(get("/api/wf/$documentId/form-data").headers(this.setAccessToken()))
            .andExpect(status().isOk)
            .andDo(print())
    }

    @DisplayName("컴포넌트 정보 조회")
    @Order(2)
    @Test
    fun getComponent() {
        val componentId = "d1e9357c226b4289a35e57fc332026f0"
        mvc.perform(get("/api/wf/component/$componentId").headers(this.setAccessToken()))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()
    }

    @DisplayName("단순문의 신청")
    @Order(3)
    @Test
    fun callDocument() {
        val componentDataList = mutableListOf<Map<String, Any>>()
        var componentData = LinkedHashMap<String, Any>()
        componentData["componentId"] = "d1e9357c226b4289a35e57fc332026f0"
        componentData["value"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "cce28b94ab5246d6b5a936aee47158a3"
        componentData["value"] = "40288ab26fa3219e016fa32231230000|Lize~"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "0159731bb17a45c0a3310d4e28d99705"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "7f328edf48d44bb39a9fcee350a6b12d"
        componentData["value"] = "homepage"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "cd69eaf70a314c16a010d7bca2c3f395"
        componentData["value"] = "oe"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "671b51096cfd4d588f32c75a4a742974"
        componentData["value"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "68756671e27441988a2c23acf40c8c91" // 담당자
        componentData["value"] = "40288ab26fa3219e016fa32231230000|Lize~"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "a38f05db61ba4aa795836c6002d9d878"
        componentData["value"] = "test"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "21e547074e47412d9c311c09b25cbabc"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "de57b451d38145498183e425aadf7cac"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "f6e4ddb773f34cec975eb1830eefe015"
        componentData["value"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "5aa5cd7a66704b9e8f8a6e794ddaf521"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "24f0fd0c2fe9446880808b72fc956aee"
        componentData["value"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "e7d99480935c4777acd2af8499f8fd16"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "1b7545051c2b4a4b9aef41284f2b4275"
        componentData["value"] = ""
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "d9097d7571844dd8b3b82e04ce88fd42"
        componentData["value"] = "none|"
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "460948cb8d68408d98e751240e8df246"
        componentData["value"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        componentDataList.add(componentData)
        componentData = LinkedHashMap()
        componentData["componentId"] = "bcbff16498a94b2082c1ec39ffcf91da"
        componentData["value"] = ""
        componentDataList.add(componentData)

        val dataMap = LinkedHashMap<String, Any>()
        dataMap["action"] = "progress"
        dataMap["assigneeId"] = "lizeelf" // 사용자 ID
        dataMap["assigneeType"] = ""
        dataMap["documentId"] = ""
        dataMap["instanceId"] = ""
        dataMap["isComplete"] = true
        dataMap["tokenId"] = ""
        dataMap["componentData"] = componentDataList
        dataMap["optionData"] = ""

        val jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataMap)
        mvc.perform(
            post("/api/wf/$documentId")
                .headers(this.setAccessToken())
                .content(jsonData)
        )
            .andExpect(status().isOk)
            .andDo(print())
    }

    @DisplayName("문서 처리 상태 조회")
    @Order(4)
    @Test
    fun getInstanceHistory() {
        // 완료된 문서 찾기
        val params = LinkedHashMap<String, Any>()
        params["userKey"] = "0509e09412534a6e98f04ca79abb6424" // ADMIN
        params["documentId"] = ""
        params["searchValue"] = ""
        params["tokenType"] = WfTokenConstants.SearchType.COMPLETED.code
        params["fromDt"] = LocalDateTime.now().minusMonths(1).atZone(ZoneOffset.UTC).toString()
        params["toDt"] = LocalDateTime.now().atZone(ZoneOffset.UTC).toString()
        params["offset"] = 0
        params["tags"] = ""
        val instances = wfInstanceService.instances(params)
        assumingThat(
            !instances.data.isNullOrEmpty()
        ) {
            val instanceId = instances.data[0].instanceId
            mvc.perform(get("/api/wf/$instanceId/history").headers(this.setAccessToken()))
                .andExpect(status().isOk)
                .andDo(print())
                .andReturn()
        }
    }

    @DisplayName("AccessToken 헤더 설정")
    private fun setAccessToken(): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        return headers
    }
}
