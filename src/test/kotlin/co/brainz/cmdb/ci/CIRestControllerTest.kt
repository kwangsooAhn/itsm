/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci

import co.brainz.cmdb.ci.constants.CIConstants
import co.brainz.cmdb.provider.dto.CIDataDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIRelationDto
import co.brainz.cmdb.provider.dto.CITagDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.UUID
import org.junit.BeforeClass
import org.junit.FixMethodOrder
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
 * CMDB CI CRUD Label 테스트 코드
 * =====================================================================================================================
 * 1. 기능 개요
 *   - CI 데이터가 실제로 CMDB에 저장되거나 변경될때 호출되는 경로에 대한 테스트이다.
 *   - CMDB를 저장,수정하는 경우는 Workflow 엔진에서 CMDB Task를 처리하는 경우이며
 *   - 이때 CI 컴포넌트에 포함된 데이터를 이용해서 신규등록, 변경, 삭제등을 호출하게 된다.
 *
 * 2. 테스트 시나리오
 *   - 테스트는 2가지 데이터를 대상으로 진행한다.
 *      1) newCIDetailDto : 신규 CI 등록
 *      2) updateCIDetailDto : 기존 CI 변경
 *
 *  3. 추후 구현 사항
 *    - CI 읽기, 삭제 기능도 완료되면 테스트 코드 추가가 필요하다.
 */

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CIRestControllerTest {
    @Autowired
    private lateinit var mvc:MockMvc

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private lateinit var newCIDto: CIDto
        private lateinit var updateCIDto: CIDto
        private var newCIDataDtoList = mutableListOf<CIDataDto>()
        private var updateCIDataDtoList = mutableListOf<CIDataDto>()
        private var newCITagList = mutableListOf<CITagDto>()
        private var updateCITagList = mutableListOf<CITagDto>()
        private var newCIRelationList = mutableListOf<CIRelationDto>()
        private var updateCIRelationList = mutableListOf<CIRelationDto>()

        @BeforeClass
        @JvmStatic
        fun setup() {

            // 데이터는 default value 로 정의된 내용을 참고

            // 신규 등록 테스트용 데이터
            newCIDto = CIDto(
                ciId = UUID.randomUUID().toString(),
                ciName = "register test CI",
                typeId = "587b4557275bcce81664db9e12485ae2", // 서버
                ciStatus = CIConstants.CIStatus.STATUS_USE.code,
                classId = null,
                ciDataList = null,
                ciRelations = null,
                ciTags = null
            )

            // 신규 등록 Attribute Data
            newCIDataDtoList.add(CIDataDto(
                newCIDto.ciId,
                "e613591ddea0f8c1f2457104f7cf286d", // 장비명
                "new server"
            ))

            newCIDataDtoList.add(CIDataDto(
                newCIDto.ciId,
                "6e247bdb7b70757e1987ae25a36c3d13", // 호스트명
                "new host"
            ))

            // 신규 등록 Tag Data
            newCITagList.add(CITagDto(
                newCIDto.ciId,
                "",
                "new tag1"
            ))

            newCITagList.add(CITagDto(
                newCIDto.ciId,
                "",
                "new tag2"
            ))

            // 신규 등록 Relation Data
            newCIRelationList.add(CIRelationDto(
                "",
                "new relation type",
                newCIDto.ciId,
                newCIDto.ciId
            ))

            newCIDto.ciDataList = newCIDataDtoList
            newCIDto.ciTags = newCITagList
            newCIDto.ciRelations = newCIRelationList

            // 변경 테스트용 데이터
            updateCIDto = newCIDto.copy()

            updateCIDto.ciName = "update test CI"
            updateCIDataDtoList.add(CIDataDto(
                newCIDto.ciId,
                "e613591ddea0f8c1f2457104f7cf286d", // 장비명
                "update server"
            ))

            updateCIDataDtoList.add(CIDataDto(
                newCIDto.ciId,
                "6e247bdb7b70757e1987ae25a36c3d13", // 호스트명
                "update host"
            ))

            // 변경용 Tag Data
            updateCITagList.add(CITagDto(
                updateCIDto.ciId,
                "",
                "update tag1"
            ))

            updateCITagList.add(CITagDto(
                updateCIDto.ciId,
                "",
                "update tag2"
            ))

            // 변경용 Relation Data
            updateCIRelationList.add(CIRelationDto(
                "",
                "update relation type",
                updateCIDto.ciId,
                updateCIDto.ciId
            ))

            updateCIDto.ciDataList = updateCIDataDtoList
            updateCIDto.ciTags = updateCITagList
            updateCIDto.ciRelations = updateCIRelationList
        }
    }
    /*
     * CI 등록 - tag, ci_data 모두 포함
     */
    @Test
    fun a_registerNewCI() {
        mvc.perform(
            post("/rest/cmdb/eg/cis")
                .content(mapper.writeValueAsString(newCIDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
        logger.debug(newCIDto.toString())
    }

    /*
     * CI 변경 - tag, ci_data 모두 포함
     */
    @Test
    fun b_modifyCI() {
        logger.debug(updateCIDto.toString())
        mvc.perform(
            put("/rest/cmdb/eg/cis/" + updateCIDto.ciId)
                .content(mapper.writeValueAsBytes(updateCIDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    /*
     * CI 조회 - 리스트 조회
     */
    @Test
    fun c_getCIList() {
        val searchValue = hashMapOf<String, Any>()
        val resultCIList = mvc.perform(
            get("/rest/cmdb/eg/cis")
                .content(mapper.writeValueAsBytes(searchValue))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        logger.debug(resultCIList)
    }

    /*
     * CI 조회 - 1건조회
     */
    @Test
    fun d_getCIView() {
        logger.debug(updateCIDto.toString())
        val resultCI = mvc.perform(
            get("/rest/cmdb/eg/cis/" + updateCIDto.ciId)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        logger.debug(resultCI)
    }

    /*
     * CI 삭제 - 실제로 삭제는 안되고 상태가 변경.
     */
    @Test
    fun e_deleteCI() {
        logger.debug(updateCIDto.toString())
        mvc.perform(
            delete("/rest/cmdb/eg/cis/" + updateCIDto.ciId)
        )
            .andExpect(status().isOk)
    }
}
