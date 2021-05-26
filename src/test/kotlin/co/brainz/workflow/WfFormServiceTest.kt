/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumeTrue
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
@DisplayName("Form API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfFormServiceTest {

    @Autowired
    private lateinit var wfFormService: WfFormService

    lateinit var userKey: String
    lateinit var formName: String

    @BeforeEach
    fun init() {
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
        this.formName = "Test Form_"
    }

    @Test
    @DisplayName("폼 생성")
    @Order(1)
    fun createForm() {
        val localDateTime = LocalDateTime.now()
        val formDto = RestTemplateFormDto(
            id = "",
            name = this.formName + localDateTime,
            desc = "테스트 코드로 등록된 임시 폼 데이터 입니다.",
            status = WfFormConstants.FormStatus.EDIT.value,
            createDt = LocalDateTime.now(),
            createUserKey = this.userKey
        )
        val saveFormDto = wfFormService.createForm(formDto)
        assumeTrue(saveFormDto.id.isNotEmpty())
        assertEquals(saveFormDto.name, this.formName + localDateTime)
    }

    @Test
    @DisplayName("폼 삭제")
    @Order(2)
    fun deleteForm() {
        val params = LinkedHashMap<String, Any>()
        params["search"] = this.formName
        val formDtoList = wfFormService.forms(params)
        assumingThat(
            formDtoList.data.isNotEmpty()
        ) { assumeTrue(wfFormService.deleteForm(formDtoList.data[0].id)) }
    }
}
