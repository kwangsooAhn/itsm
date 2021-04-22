/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
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
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(get("/api/wf/$documentId/form-data").headers(headers))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("컴포넌트 정보 조회")
    @Order(2)
    @Test
    fun getComponent() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        val componentId = "d1e9357c226b4289a35e57fc332026f0"
        val a = mvc.perform(get("/api/wf/component/$componentId").headers(headers))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }
}
