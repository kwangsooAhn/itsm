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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * API AccessToken, RefreshToken Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("API Token Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TokenTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    lateinit var bodyContent: String

    @BeforeEach
    fun init() {
        val params = LinkedHashMap<String, Any>()
        params["userId"] = "lizeelf"
        params["password"] = "itsm123123"
        this.bodyContent = mapper.writeValueAsString(params)
    }

    @Test
    @DisplayName("Access Token 발행")
    @Order(1)
    fun getAccessToken() {
        mvc.perform(get("/api/tokens").content(this.bodyContent))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("Refresh Token 으로 Access Token 재발행")
    @Order(2)
    fun getRefreshTokenToAccessToken() {
        val accessResult = mvc.perform(get("/api/tokens").content(bodyContent))
            .andExpect(status().isOk)
            .andReturn()
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        val resultMap: LinkedHashMap<String, Any> =
            mapper.readValue(accessResult.response.contentAsString, linkedMapType)

        val params = LinkedHashMap<String, Any>()
        params["refresh_token"] = resultMap["refresh_token"].toString()
        val bodyContent = mapper.writeValueAsString(params)
        mvc.perform(get("/api/tokens/refresh").content(bodyContent))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }
}
