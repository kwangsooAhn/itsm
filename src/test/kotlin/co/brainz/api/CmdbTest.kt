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
import org.junit.jupiter.api.Disabled
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("API Token Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CmdbTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    lateinit var accessToken: String

    @BeforeEach
    fun init() {
        // get access token
        val params = LinkedHashMap<String, Any>()
        params["userId"] = "lizeelf"
        params["password"] = "itsm123123"
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

    @Test
    @DisplayName("CI 목록 조회")
    @Order(1)
    fun getCIs() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(
            get("/api/cmdb/cis")
                .param("limit", 5.toString())
                .headers(headers)
        )
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("CI Attribute 목록 조회")
    @Order(2)
    fun getCIAttributes() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(
            get("/api/cmdb/attributes")
                .param("search", "Availability")
                .headers(headers)
        )
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("CI Type 목록 조회")
    @Order(3)
    fun getCITypes() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(
            get("/api/cmdb/types")
                .param("offset", 3.toString())
                .param("limit", 10.toString())
                .headers(headers)
        )
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("CI Class 목록 조회")
    @Order(4)
    fun getClasses() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(
            get("/api/cmdb/classes")
                .param("search", "Server")
                .headers(headers)
        )
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("CI Class 삭제 (임시 데이터)")
    @Order(5)
    @Disabled
    fun deleteClass() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        mvc.perform(
            delete("/api/cmdb/classes/40288ae2781068ef01781096d7d80002")
                .headers(headers)
        )
            .andExpect(status().isOk)
    }
}
