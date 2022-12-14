/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIDto
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import com.google.gson.JsonArray
import javax.transaction.Transactional
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

@Disabled
@SpringBootTest
@DisplayName("CI 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class CIServiceTest {

    @Autowired
    private lateinit var ciService: CIService

    lateinit var ciName: String
    lateinit var typeId: String

    @BeforeEach
    fun init() {
        this.ciName = "Test CI 1"
        this.typeId = "f18c064040304e493f4dc7385595601f" // 네트워크
    }

    @Test
    @DisplayName("CI 전체 갯수 조회")
    @Order(1)
    fun getAllCICountCheck() {
        val ciDtoList = ciService.getCIs(CISearchCondition())
        assumingThat(
            ciDtoList.data.isNotEmpty()
        ) {
            assertTrue(ciDtoList.paging.totalCount > 0)
        }
    }

    @Test
    @DisplayName("CI 생성")
    @Order(2)
    fun createCI() {
        val ciDto = CIDto(
            ciId = AliceUtil().getUUID(),
            ciNo = "",
            ciName = this.ciName,
            classId = "",
            interlink = false,
            ciStatus = RestTemplateConstants.CIStatus.STATUS_USE.code,
            ciDesc = "",
            typeId = this.typeId,
            ciIcon = "",
            ciDataList = mutableListOf(),
            ciRelations = mutableListOf(),
            ciTags = JsonArray()
        )
        val returnDto = ciService.createCI(ciDto)
        assertEquals(returnDto.code, "0")
        assertTrue(returnDto.status)
    }

    @Test
    @DisplayName("CI Name 검색어 조회")
    @Order(3)
    fun getCISearch() {
        val ciDtoList = ciService.getCIs(CISearchCondition(
            searchValue = this.ciName
        ))
        assumingThat(
            ciDtoList.data.isNotEmpty()
        ) {
            assertEquals(ciDtoList.data[0].ciName, this.ciName)
        }
    }

    @Test
    @DisplayName("CI 단일 조회")
    @Order(4)
    fun getCI() {
        var ciId = ""
        val ciDtoList = ciService.getCIs(CISearchCondition(
            searchValue = this.ciName
        ))
        if (!ciDtoList.data.isNullOrEmpty()) {
            ciId = ciDtoList.data[0].ciId.toString()
        }
        assumingThat(
            ciId.isNotEmpty()
        ) {
            val ciDto = ciService.getCIDetail(ciId)
            assertEquals(ciDto.ciNo, ciDtoList.data[0].ciNo)
            assertEquals(ciDto.ciName, ciDtoList.data[0].ciName)
            assertEquals(ciDto.ciDesc, ciDtoList.data[0].ciDesc)
            assertEquals(ciDto.typeId, ciDtoList.data[0].typeId)
            assertEquals(ciDto.ciStatus, ciDtoList.data[0].ciStatus)
        }
    }

    @Test
    @DisplayName("CI 수정")
    @Order(5)
    fun updateCI() {
        val ciDtoList = ciService.getCIs(CISearchCondition(
            searchValue = this.ciName
        ))
        assumingThat(
            ciDtoList.data.isNotEmpty()
        ) {
            for (ciDto in ciDtoList.data) {
                if (ciDto.ciId != null) {
                    val updateCiDto = CIDto(
                        ciId = ciDto.ciId.toString(),
                        ciNo = ciDto.ciNo,
                        ciName = ciDto.ciName.toString(),
                        ciDesc = "Update Test 1",
                        typeId = ciDto.typeId.toString(),
                        classId = ciDto.classId,
                        ciIcon = ciDto.ciIcon
                    )
                    assertTrue(ciService.updateCI(updateCiDto).status)
                }
            }
        }
    }

    @Test
    @DisplayName("CI 삭제")
    @Order(6)
    fun deleteCI() {
        val ciDtoList = ciService.getCIs(CISearchCondition(
            searchValue = this.ciName
        ))
        assumingThat(
            ciDtoList.data.isNotEmpty()
        ) {
            for (ciDto in ciDtoList.data) {
                val ci = CIDto(
                    ciId = ciDto.ciId.toString(),
                    ciStatus = "",
                    typeId = ""
                )
                assertTrue(ciService.deleteCI(ci).status)
            }
        }
    }
}
