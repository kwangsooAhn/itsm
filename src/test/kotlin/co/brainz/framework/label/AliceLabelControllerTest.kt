/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label

import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.entity.AliceLabelEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.UUID
import kotlin.collections.HashMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Label 테스트 코드
 * =====================================================================================================================
 * 1. 기능 개요
 *   - 라벨은 ajax통신을 위한 URL을 제공하여 실시간으로 CRUD를 제공할 수 있다.
 *   - 그와 동시에 라벨기능은 Framwork 패키지에 속해 있어 어떤 서비스든 AliceLabelService의 메소드를 호출하여 바로 사용할 수도 있다.
 *   - 라벨은 Key, Value 구성이며 Value는 null이 가능하다.
 *
 * 2. 테스트 시나리오
 *   - 테스트는 5가지 데이터를 대상으로 진행한다.
 *      1) labelDtoForKeyOnly : 키만 있는 라벨 1개
 *      2) labelDtoForKeyValue : 키와 값이 모두 있는 라벨 1개
 *      3) labelsDtoForKeyOnly : 키만 있는 라벨 2개
 *      4) labelsDtoForKeyValue : 키와 값이 모두 있는 라벨 2개
 *      5) noExistLabelsDto : 업데이트, 삭제 테스트를 위한 존재하지 않는 라벨
 *
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AliceLabelControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val labelTarget: String = "component"
        private val labelForKeyOnly = HashMap<String, String?>()
        private val labelForKeyValue = HashMap<String, String?>()
        private val labelsForKeyOnly = HashMap<String, String?>()
        private val labelsForKeyValue = HashMap<String, String?>()
        private val noExistLabel = HashMap<String, String?>()

        private lateinit var labelDtoForKeyOnly: AliceLabelDto
        private lateinit var labelDtoForKeyValue: AliceLabelDto
        private lateinit var labelsDtoForKeyOnly: AliceLabelDto
        private lateinit var labelsDtoForKeyValue: AliceLabelDto
        private lateinit var noExistLabelsDto: AliceLabelDto

        @BeforeClass
        @JvmStatic
        fun setup() {
            labelForKeyOnly["KeyOfLabelForKeyOnly"] = null
            labelDtoForKeyOnly = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelForKeyOnly
            )

            labelForKeyValue["KeyOfLabelForKeyValue"] = "ValueOfLabelForKeyValue"
            labelDtoForKeyValue = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelForKeyValue
            )

            labelsForKeyOnly["Key1OfLabelsForKeyOnly"] = null
            labelsForKeyOnly["Key2OfLabelsForKeyOnly"] = null
            labelsDtoForKeyOnly = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelsForKeyOnly
            )

            labelsForKeyValue["Key1OfLabelsForKeyValue"] = "Value1OfLabelsForKeyValue"
            labelsForKeyValue["Key2OfLabelsForKeyValue"] = "Value2OfLabelsForKeyValue"
            labelsDtoForKeyValue = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelsForKeyValue
            )

            noExistLabel["noExistLabel"] = null
            noExistLabelsDto = AliceLabelDto(
                labelTarget = "labelTarget",
                labelTargetId = "labelTargetId",
                labels = noExistLabel
            )
        }
    }

    @Test
    @Ignore("Jenkins Issue")
    fun a_insertLabelOnlyKey() {
        mvc.perform(
            post("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyOnly))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun b_insertLabelKeyValue() {
        mvc.perform(
            post("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun c_insertDuplicateLabel() {
        mvc.perform(
            post("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyOnly))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun d_insertLabelsForKeyOnly() {
        mvc.perform(
            post("/rest/labels")
                .content(mapper.writeValueAsString(labelsDtoForKeyOnly))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun e_insertLabelsForKeyValue() {
        mvc.perform(
            post("/rest/labels")
                .content(mapper.writeValueAsString(labelsDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun f_getLabel() {
        val resultLabel = mvc.perform(
            get("/rest/labels")
                .param("label_target", labelDtoForKeyOnly.labelTarget)
                .param("label_target_id", labelDtoForKeyOnly.labelTargetId)
                .param("label_key", "KeyOfLabelForKeyOnly")
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val responseData: MutableList<AliceLabelEntity> = mapper.readValue(
            resultLabel,
            mapper.typeFactory.constructCollectionType(MutableList::class.java, AliceLabelEntity::class.java)
        )

        assertThat(responseData.size).isEqualTo(1)
        assertThat(responseData[0].labelKey).isEqualTo("KeyOfLabelForKeyOnly")
        logger.info(responseData[0].toString())
    }

    @Test
    @Ignore("Jenkins Issue")
    fun g_getLabels() {
        val resultLabel = mvc.perform(
            get("/rest/labels")
                .param("label_target", labelsDtoForKeyOnly.labelTarget)
                .param("label_target_id", labelsDtoForKeyOnly.labelTargetId)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val responseData: MutableList<AliceLabelEntity> = mapper.readValue(
            resultLabel,
            mapper.typeFactory.constructCollectionType(MutableList::class.java, AliceLabelEntity::class.java)
        )

        assertThat(responseData.size).isEqualTo(2)
        responseData.forEach {
            logger.info(it.toString())
        }
    }

    @Test
    @Ignore("Jenkins Issue")
    fun h_updateLabel() {
        logger.info("before label : $labelDtoForKeyValue")
        labelDtoForKeyValue.labels?.set("KeyOfLabelForKeyValue", "UpdatedValueOfLabelForKeyValue")
        mvc.perform(
            put("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        val resultLabel = mvc.perform(
            get("/rest/labels")
                .param("label_target", labelDtoForKeyValue.labelTarget)
                .param("label_target_id", labelDtoForKeyValue.labelTargetId)
                .param("label_key", "KeyOfLabelForKeyValue")
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val responseData: MutableList<AliceLabelEntity> = mapper.readValue(
            resultLabel,
            mapper.typeFactory.constructCollectionType(MutableList::class.java, AliceLabelEntity::class.java)
        )

        assertThat(responseData.size).isEqualTo(1)
        assertThat(responseData[0].labelValue).isEqualTo("UpdatedValueOfLabelForKeyValue")
        logger.info("after label : " + responseData[0].toString())
    }

    @Test
    @Ignore("Jenkins Issue")
    fun i_updateLabels() {
        logger.info("before labels : $labelsDtoForKeyValue")
        labelsDtoForKeyValue.labels?.set("Key1OfLabelsForKeyValue", "UpdatedValue1OfLabelsForKeyValue")
        labelsDtoForKeyValue.labels?.set("Key2OfLabelsForKeyValue", "UpdatedValue2OfLabelsForKeyValue")
        mvc.perform(
            put("/rest/labels")
                .content(mapper.writeValueAsString(labelsDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        val resultLabel = mvc.perform(
            get("/rest/labels")
                .param("label_target", labelsDtoForKeyValue.labelTarget)
                .param("label_target_id", labelsDtoForKeyValue.labelTargetId)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val responseData: MutableList<AliceLabelEntity> = mapper.readValue(
            resultLabel,
            mapper.typeFactory.constructCollectionType(MutableList::class.java, AliceLabelEntity::class.java)
        )

        assertThat(responseData.size).isEqualTo(2)

        responseData.forEach {
            logger.info("after labels : $it")
        }
    }

    @Test
    @Ignore("Jenkins Issue")
    fun j_updateNoExistLabel() {
        mvc.perform(
            put("/rest/labels")
                .content(mapper.writeValueAsString(noExistLabelsDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        logger.info(noExistLabelsDto.toString())
    }

    @Test
    @Ignore("Jenkins Issue")
    fun k_deleteLabel() {
        mvc.perform(
            delete("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyOnly))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        mvc.perform(
            delete("/rest/labels")
                .content(mapper.writeValueAsString(labelDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun l_deleteLabels() {
        mvc.perform(
            delete("/rest/labels")
                .content(mapper.writeValueAsString(labelsDtoForKeyOnly))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        mvc.perform(
            delete("/rest/labels")
                .content(mapper.writeValueAsString(labelsDtoForKeyValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    @Ignore("Jenkins Issue")
    fun m_deleteNoExistLabel() {
        mvc.perform(
            delete("/rest/labels")
                .content(mapper.writeValueAsString(noExistLabelsDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }
}
