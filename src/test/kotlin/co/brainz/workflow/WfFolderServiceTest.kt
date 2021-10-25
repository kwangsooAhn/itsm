/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.itsm.folder.service.FolderService
import co.brainz.workflow.token.service.WfTokenService
import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assumptions
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
 * 신청서가 진행될 때 instance가 생성되면서 folder(relate_type=origin)도 생성된다.
 * 문서 상세에서 연관문서를 추가할 경우 folder에 related_type이 reference로 추가된다.
 */
@Disabled
@SpringBootTest
@DisplayName("Folder API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class WfFolderServiceTest {

    @Autowired
    private lateinit var folderService: FolderService

    @Autowired
    private lateinit var wfTokenService: WfTokenService

    lateinit var tokenId: String

    @BeforeEach
    fun init() {
        this.tokenId = "40288a19724f5b9201724f5d11760001"
    }

    @Test
    @DisplayName("토큰 정보로 origin 폴더 조회")
    @Order(1)
    fun getOriginFolder() {
        val tokenDto = wfTokenService.getToken(this.tokenId)
        Assumptions.assumingThat(
            tokenDto.tokenId.isNotEmpty()
        ) {
            val folderDto = folderService.getFolderId(tokenId)
            assertNotNull(folderDto)
        }
    }
}
