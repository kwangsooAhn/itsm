/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumingThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * CI Attribute Test
 */
@Disabled
@SpringBootTest
@DisplayName("CI Attribute 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CIAttributeServiceTest {

    @Autowired
    private lateinit var ciAttributeService: CIAttributeService

    lateinit var userKey: String
    lateinit var attributeName: String

    @BeforeEach
    fun init() {
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
        this.attributeName = "Test Attribute 1"
    }

    @Test
    @DisplayName("Attribute 전체 갯수 조회")
    @Order(1)
    fun getAllAttributeCountCheck() {
        val attributeDtoList = ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = "",
            pageNum = 0L,
            contentNumPerPage = 0L
        ))
        assumingThat(
            attributeDtoList.data.isNotEmpty()
        ) {
            assertTrue(attributeDtoList.paging.totalCount > 0)
        }
    }

    @Test
    @DisplayName("Attribute Name 검색어 조회")
    @Order(2)
    fun getAttributeSearch() {
        val searchValue = "Availability"
        val attributeDtoList = ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = searchValue,
            pageNum = 0L,
            contentNumPerPage = 0L
        ))
        assumingThat(
            attributeDtoList.data.isNotEmpty()
        ) {
            assertEquals(attributeDtoList.data[0].attributeName, searchValue)
        }
    }

    @Test
    @DisplayName("Attribute 단일 조회")
    @Order(3)
    fun getAttribute() {
        var attributeId = ""
        val attributeDtoList = ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = ""
        ))
        if (!attributeDtoList.data.isNullOrEmpty()) {
            attributeId = attributeDtoList.data[0].attributeId.toString()
        }
        assumingThat(
            attributeId.isNotEmpty()
        ) {
            val attributeDto = ciAttributeService.getCIAttribute(attributeId)
            assertEquals(attributeDto.attributeName, attributeDtoList.data[0].attributeName)
            assertEquals(attributeDto.attributeDesc, attributeDtoList.data[0].attributeDesc)
            assertEquals(attributeDto.attributeText, attributeDtoList.data[0].attributeText)
            assertEquals(attributeDto.attributeType, attributeDtoList.data[0].attributeType)
        }
    }

    @Test
    @DisplayName("Attribute 생성")
    @Order(4)
    fun createAttribute() {
        val ciAttributeDto = CIAttributeDto(
            attributeId = "",
            attributeName = this.attributeName,
            attributeDesc = "",
            attributeType = "inputbox",
            attributeText = "테스트 속성 1",
            attributeValue = "{\"validate\":\"\",\"required\":\"false\",\"maxLength\":\"100\",\"minLength\":\"0\"}",
            createUserKey = this.userKey,
            createDt = LocalDateTime.now()
        )
        val returnDto = ciAttributeService.createCIAttribute(ciAttributeDto)
        assertEquals(returnDto.code, "0")
        assertTrue(returnDto.status)
    }

    @Test
    @DisplayName("Attribute 수정")
    @Order(5)
    fun updateAttribute() {
        val params = LinkedHashMap<String, Any>()
        val attributeDtoList = ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = this.attributeName
        ))
        assumingThat(
            attributeDtoList.data.isNotEmpty()
        ) {
            for (attributeDto in attributeDtoList.data) {
                val attribute = ciAttributeService.getCIAttribute(attributeDto.attributeId.toString())
                val updateAttributeDto = CIAttributeDto(
                    attributeId = attribute.attributeId.toString(),
                    attributeName = attribute.attributeName.toString(),
                    attributeValue = "",
                    attributeDesc = "Update Test 1",
                    attributeText = attribute.attributeText.toString(),
                    attributeType = attribute.attributeType,
                    updateUserKey = this.userKey,
                    updateDt = LocalDateTime.now()
                )
                assertTrue(ciAttributeService.updateCIAttribute(updateAttributeDto).status)
            }
        }
    }

    @Test
    @DisplayName("Attribute 삭제")
    @Order(6)
    fun deleteAttribute() {
        val attributeDtoList = ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = this.attributeName
        ))
        assumingThat(
            attributeDtoList.data.isNotEmpty()
        ) {
            for (attributeDto in attributeDtoList.data) {
                assertTrue(ciAttributeService.deleteCIAttribute(attributeDto.attributeId.toString()))
            }
        }
    }
}
