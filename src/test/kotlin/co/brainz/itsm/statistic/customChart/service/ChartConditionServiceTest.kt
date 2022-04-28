/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customChart.service

import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("Chart condition service test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class ChartConditionServiceTest {
    @Autowired
    private lateinit var chartConditionService :ChartConditionService

    @Test
    @DisplayName("tag parsing")
    @Order(1)
    fun getTagsInCondition() {
        val tagString1 = "[tag]"              // 태그 1개
        val tagString2 = "[tag"               // 태그 없음
        val tagString3 = "[tag1][tag2]"       // 태그 복수개
        val tagString4 = "tag"                // 태그 없음
        val tagString5 = "[tag1][tag2][tag1]" // 중복 태그 포함
        val tagString6 = ""                   // 빈값

        val method = chartConditionService::class.java.getDeclaredMethod("getTagsInCondition", String::class.java)
        method.trySetAccessible()

        val result1 = method.invoke(chartConditionService, tagString1)
        val result2 = method.invoke(chartConditionService, tagString2)
        val result3 = method.invoke(chartConditionService, tagString3)
        val result4 = method.invoke(chartConditionService, tagString4)
        val result5 = method.invoke(chartConditionService, tagString5)
        val result6 = method.invoke(chartConditionService, tagString6)

        Assertions.assertAll(
            Executable { Assertions.assertEquals(setOf("tag"), result1) },
            Executable { Assertions.assertEquals(setOf<String>(), result2) },
            Executable { Assertions.assertEquals(setOf("tag1","tag2"), result3) },
            Executable { Assertions.assertEquals(setOf<String>(), result4) },
            Executable { Assertions.assertEquals(setOf("tag1","tag2"), result5) },
            Executable { Assertions.assertEquals(setOf<String>(), result6) }
        )
    }
}
