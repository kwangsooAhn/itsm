/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
@Disabled
@SpringBootTest
@DisplayName("Component API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfComponentServiceTest {

    // @Autowired
    // private lateinit var wfComponentService: WfComponentService
    //
    // @Test
    // @DisplayName("ComponentData 목록 중에서 custom-code 조회하여 CustomCode 아이디 목록 리턴")
    // @Order(1)
    // fun dd() {
    //     val params = LinkedHashMap<String, Any>()
    //     params["componentType"] = CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE
    //     params["componentAttribute"] = CustomCodeConstants.PROPERTY_ID_DISPLAY
    //     val customCodeIds = wfComponentService.getComponentDataCustomCodeIds(params)
    //     assumeTrue(customCodeIds.isNotEmpty())
    // }
}
