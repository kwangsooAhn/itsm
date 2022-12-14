package co.brainz.cmdb.attribute

/*
import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
*/

/*@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)*/
class AttributeTest {

    /*@Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    @Test
    fun attribute_all_data() {
        mockMvc.perform(get("/rest/cmdb/eg/attributes"))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    *//**
     * Attribute attributeType ?????? ?????? ????????? ??????.
     *//*
    @Test
    fun attributes_search_attribute_type_multi() {
        val result = mockMvc.perform(get("/rest/cmdb/eg/attributes").param("search", "inputbox"))
            .andExpect(status().isOk)
            .andReturn()
        val content = result.response.contentAsString
        val responseData: List<CIAttributeListDto> = mapper.readValue(
            content,
            mapper.typeFactory.constructCollectionType(List::class.java, CIAttributeListDto::class.java)
        )
        assertThat(responseData.size).isEqualTo(responseData[0].totalCount)
    }

    *//**
     * Attribute attributeName ?????? ????????? 1??? ??????.
     *//*
    @Test
    fun attributes_search_attribute_name_one() {
        mockMvc.perform(get("/rest/cmdb/eg/attributes").param("search", "Classification"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].attributeName").value("Classification"))
            .andExpect(jsonPath("$.[0].attributeType").value("inputbox"))
            .andExpect(jsonPath("$.[0].attributeText").value("??????"))
    }

    *//**
     * Attribute ????????? ??????.
     *//*
    @Test
    fun attribute_insert_one() {
        val attributeDto = CIAttributeDto(
            attributeId = "",
            attributeName = "TestData",
            attributeText = "TestData",
            attributeType = "inputbox",
            createDt = LocalDateTime.now(),
            createUserKey = "0509e09412534a6e98f04ca79abb6424"
        )
        val attributeId = attributes_search_attribute_id(attributeDto.attributeName)
        if (attributeId != null) {
            mockMvc.perform(delete("/rest/cmdb/eg/attributes/$attributeId"))
                .andExpect(status().isOk)
        }
        val data = mapper.writeValueAsString(attributeDto)
        val result = mockMvc.perform(
            post("/rest/cmdb/eg/attributes")
                .content(data)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val content = result.response.contentAsString
        val responseData: RestTemplateReturnDto = mapper.readValue(
            content,
            RestTemplateReturnDto::class.java
        )
        assertThat(responseData.status).isEqualTo(true)
        assertThat(responseData.code).isEqualTo("0")
    }

    @Test
    fun attributes_update_one() {
        attribute_insert_one()
        val attributeId = attributes_search_attribute_id("TestData")
        val attributeDto = CIAttributeDto(
            attributeId = attributeId!!,
            attributeName = "TestData",
            attributeText = "TestData",
            attributeType = "inputbox",
            updateDt = LocalDateTime.now(),
            updateUserKey = "0509e09412534a6e98f04ca79abb6424"
        )

        val data = mapper.writeValueAsString(attributeDto)
        val result = mockMvc.perform(
            put("/rest/cmdb/eg/attributes/$attributeId")
                .content(data)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val content = result.response.contentAsString
        val responseData: RestTemplateReturnDto = mapper.readValue(
            content,
            RestTemplateReturnDto::class.java
        )
        assertThat(responseData.status).isEqualTo(true)
        assertThat(responseData.code).isEqualTo("0")
    }

    *//**
     * Attribute ??????.
     *//*
    @Test
    fun attributes_delete_by_attributeId() {
        val attributeId = attributes_search_attribute_id("TestData")
        if (attributeId != null) {
            mockMvc.perform(delete("/rest/cmdb/eg/attributes/$attributeId"))
                .andExpect(status().isOk)
        }
    }

    *//**
     * AttributeName ?????? AttributeId ??????.
     *//*
    private fun attributes_search_attribute_id(attributeName: String): String? {
        val result = mockMvc.perform(get("/rest/cmdb/eg/attributes").param("search", attributeName))
            .andExpect(status().isOk)
            .andReturn()
        val content = result.response.contentAsString
        var attributeId: String? = null
        if (content.contains("attributeId")) {
            val responseData: List<CIAttributeListDto> = mapper.readValue(
                content,
                mapper.typeFactory.constructCollectionType(List::class.java, CIAttributeListDto::class.java)
            )
            if (responseData.isNotEmpty()) {
                attributeId = responseData[0].attributeId
            }
        }
        return attributeId
    }*/
}
