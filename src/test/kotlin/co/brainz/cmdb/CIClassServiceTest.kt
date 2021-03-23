/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.provider.dto.CIClassDto
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions
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
@DisplayName("CI Class 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CIClassServiceTest {

    @Autowired
    private lateinit var ciClassService: CIClassService

    @Autowired
    private lateinit var ciAttributeService: CIAttributeService

    lateinit var userKey: String
    lateinit var className: String

    @BeforeEach
    fun init() {
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
        this.className = "Test Class 1"
    }

    @Test
    @DisplayName("Class 전체 갯수 조회")
    @Order(1)
    fun getAllClassCountCheck() {
        val params = LinkedHashMap<String, Any>()
        val ciClassDtoList = ciClassService.getCIClasses(params)
        assumingThat(
            ciClassDtoList.data.isNotEmpty()
        ) {
            assertTrue(ciClassDtoList.totalCount > 0)
        }
    }

    @Test
    @DisplayName("Class Name 검색어 조회")
    @Order(2)
    fun getClassSearch() {
        val searchValue = "Server"
        val params = LinkedHashMap<String, Any>()
        params["search"] = searchValue
        val ciClassDtoList = ciClassService.getCIClasses(params)
        assumingThat(
            ciClassDtoList.data.isNotEmpty()
        ) {
            Assertions.assertEquals(ciClassDtoList.data[0].className, searchValue)
        }
    }

    @Test
    @DisplayName("Class 단일 조회")
    @Order(3)
    fun getClass() {
        var classId = ""
        val params = LinkedHashMap<String, Any>()
        val ciClassDtoList = ciClassService.getCIClasses(params)
        if (!ciClassDtoList.data.isNullOrEmpty()) {
            classId = ciClassDtoList.data[0].classId
        }
        assumingThat(
            classId.isNotEmpty()
        ) {
            val classDto = ciClassService.getCIClass(classId)
            Assertions.assertEquals(classDto.className, ciClassDtoList.data[0].className)
            Assertions.assertEquals(classDto.classDesc, ciClassDtoList.data[0].classDesc)
        }
    }

    @Test
    @DisplayName("Class 생성(attributes 포함)")
    @Order(4)
    fun createClass() {
        val attributes = mutableListOf<String>()
        val params = LinkedHashMap<String, Any>()
        params["offset"] = 1
        val attributeList = ciAttributeService.getCIAttributes(params)
        attributeList.data.forEachIndexed { index, attribute ->
            if (index < 5) {
                attributes.add(attribute.attributeId.toString())
            }
        }
        val ciClassDto = CIClassDto(
            classId = "",
            className = this.className,
            classDesc = "",
            classLevel = 1,
            pClassId = "root",
            attributes = attributes,
            createDt = LocalDateTime.now(),
            createUserKey = this.userKey
        )
        assertTrue(ciClassService.createCIClass(ciClassDto))
    }

    @Test
    @DisplayName("Class 수정")
    @Order(5)
    @Transactional
    fun updateClass() {
        val params = LinkedHashMap<String, Any>()
        params["search"] = this.className
        val ciClassDtoList = ciClassService.getCIClasses(params)
        assumingThat(
            ciClassDtoList.data.isNotEmpty()
        ) {
            val updateAttributes = mutableListOf<String>()
            for (ciClassDto in ciClassDtoList.data) {
                if (ciClassDto.className == this.className) {
                    val ciClassAttributeList = ciClassService.getCIClassAttributes(ciClassDto.classId)
                    ciClassAttributeList.forEach { ciClass ->
                        ciClass.attributes?.forEach { attribute ->
                            updateAttributes.add(attribute.attributeId)
                        }
                    }

                    val updateCiClassDto = CIClassDto(
                        classId = ciClassDto.classId,
                        className = ciClassDto.className,
                        classDesc = "Update Test 1",
                        classLevel = ciClassDto.classLevel,
                        attributes = updateAttributes,
                        pClassId = ciClassDto.pClassId,
                        updateDt = LocalDateTime.now(),
                        updateUserKey = this.userKey
                    )
                    assertTrue(ciClassService.updateCIClass(updateCiClassDto))
                }
            }
        }
    }

    @Test
    @DisplayName("Class 삭제")
    @Order(6)
    fun deleteClass() {
        val params = LinkedHashMap<String, Any>()
        params["search"] = this.className
        val ciClassDtoList = ciClassService.getCIClasses(params)
        assumingThat(
            ciClassDtoList.data.isNotEmpty()
        ) {
            for (ciClassDto in ciClassDtoList.data) {
                if (ciClassDto.className == this.className) {
                    assertTrue(ciClassService.deleteCIClass(ciClassDto.classId))
                }
            }
        }
    }
}
