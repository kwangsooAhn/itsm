/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb

import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CITypeDto
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
@DisplayName("CI Type 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CITypeServiceTest {

    @Autowired
    private lateinit var ciTypeService: CITypeService

    lateinit var typeName: String
    lateinit var userKey: String

    @BeforeEach
    fun init() {
        this.typeName = "Test Type 1"
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
    }

    @Test
    @DisplayName("Type 전체 갯수 조회")
    @Order(1)
    fun getAllTypeCountCheck() {
        val params = LinkedHashMap<String, Any>()
        val ciTypeDtoList = ciTypeService.getCITypes(params)
        assumingThat(
            ciTypeDtoList.data.isNotEmpty()
        ) {
            assertTrue(ciTypeDtoList.totalCount > 0)
        }
    }

    @Test
    @DisplayName("Type Name 검색어 조회")
    @Order(2)
    fun getTypeSearch() {
        val searchValue = "서버"
        val params = LinkedHashMap<String, Any>()
        params["search"] = searchValue
        val ciTypeDtoList = ciTypeService.getCITypes(params)
        assumingThat(
            ciTypeDtoList.data.isNotEmpty()
        ) {
            assertEquals(ciTypeDtoList.data[0].typeName, searchValue)
        }
    }

    @Test
    @DisplayName("Type 단일 조회")
    @Order(3)
    fun getType() {
        var typeId = ""
        val params = LinkedHashMap<String, Any>()
        val ciTypeDtoList = ciTypeService.getCITypes(params)
        if (!ciTypeDtoList.data.isNullOrEmpty()) {
            typeId = ciTypeDtoList.data[0].typeId.toString()
        }
        assumingThat(
            typeId.isNotEmpty()
        ) {
            val typeDto = ciTypeService.getCITypeDetail(typeId)
            assertEquals(typeDto.typeName, ciTypeDtoList.data[0].typeName)
            assertEquals(typeDto.typeAlias, ciTypeDtoList.data[0].typeAlias)
            assertEquals(typeDto.typeDesc, ciTypeDtoList.data[0].typeDesc)
        }
    }

    @Test
    @DisplayName("Type 생성")
    @Order(4)
    fun createType() {
        val ciTypeDto = CITypeDto(
            typeId = "",
            typeName = this.typeName,
            typeAlias = "TestAlias",
            typeDesc = "",
            typeLevel = 1,
            defaultClassId = "df562114ab87c066adeaea79b2e4a8a2",
            pTypeId = "root",
            createDt = LocalDateTime.now(),
            createUserKey = this.userKey
        )
        assertTrue(ciTypeService.createCIType(ciTypeDto))
    }

    @Test
    @DisplayName("Type 수정")
    @Order(5)
    fun updateType() {
        val params = LinkedHashMap<String, Any>()
        params["search"] = this.typeName
        val ciTypeDtoList = ciTypeService.getCITypes(params)
        assumingThat(
            ciTypeDtoList.data.isNotEmpty()
        ) {
            for (ciTypeDto in ciTypeDtoList.data) {
                if (ciTypeDto.typeName == this.typeName) {
                    val updateTypeDto = CITypeDto(
                        typeId = ciTypeDto.typeId.toString(),
                        typeName = ciTypeDto.typeName,
                        typeDesc = "Update Test 1",
                        typeAlias = ciTypeDto.typeAlias,
                        typeLevel = ciTypeDto.typeLevel,
                        pTypeId = "root",
                        defaultClassId = "df562114ab87c066adeaea79b2e4a8a2",
                        updateUserKey = this.userKey,
                        updateDt = LocalDateTime.now()
                    )
                    assertTrue(ciTypeService.updateCIType(ciTypeDto.typeId.toString(), updateTypeDto))
                }
            }
        }
    }

    @Test
    @DisplayName("Type 삭제")
    @Order(6)
    fun deleteType() {
        val params = LinkedHashMap<String, Any>()
        params["search"] = this.typeName
        val ciTypeDtoList = ciTypeService.getCITypes(params)
        assumingThat(
            ciTypeDtoList.data.isNotEmpty()
        ) {
            for (ciTypeDto in ciTypeDtoList.data) {
                if (ciTypeDto.typeName == this.typeName) {
                    assertTrue(ciTypeService.deleteCIType(ciTypeDto.typeId.toString()))
                }
            }
        }
    }
}
