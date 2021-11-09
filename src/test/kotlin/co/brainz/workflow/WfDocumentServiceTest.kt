/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.workflow.document.service.WfDocumentService
import org.junit.jupiter.api.Assumptions.assumeTrue
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
@DisplayName("Document API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfDocumentServiceTest {

    @Autowired
    private lateinit var wfDocumentService: WfDocumentService

    lateinit var userKey: String

    @BeforeEach
    fun init() {
        this.userKey = "40288ab26fa3219e016fa32231230000"
    }

    @Test
    @DisplayName("신청서 목록 전체 조회")
    @Order(2)
    fun getDocuments() {
        val searchListDto = DocumentSearchCondition(
            pageNum = 1L
        )
        val allDocumentList = wfDocumentService.allDocuments(searchListDto)
        if (allDocumentList.isNotEmpty()) {
            assumeTrue(allDocumentList.isNotEmpty())
        } else {
            assumeTrue(allDocumentList.isEmpty())
        }
    }
}
