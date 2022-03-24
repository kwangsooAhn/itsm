/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.itsm.customCode.service.CustomCodeService
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("CustomCode 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CustomCodeServiceTest {

    @Autowired
    private lateinit var customCodeService: CustomCodeService

    @Test
    @DisplayName("컴포넌트에서 사용중인 커스텀코드 리스트를 리턴")
    @Order(1)
    fun getUsedCustomCodeIdListTest() {
        assumeTrue(customCodeService.getUsedCustomCodeIdList().isNotEmpty())
    }
}
