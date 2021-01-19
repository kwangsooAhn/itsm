/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label

import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.service.AliceLabelService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.collections.HashMap

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DisplayName("라벨링 서비스 테스트")
class AliceLabelServiceTest {

    @Autowired
    private lateinit var labelService: AliceLabelService

    companion object {
        private const val labelTarget: String = "component"
        private val labelForKeyOnly = HashMap<String, String?>()
        private val labelForKeyValue = HashMap<String, String?>()
        private val labelsForKeyOnly = HashMap<String, String?>()
        private val labelsForKeyValue = HashMap<String, String?>()

        lateinit var labelDtoForKeyOnly: AliceLabelDto
        lateinit var labelDtoForKeyValue: AliceLabelDto
        lateinit var labelsDtoForKeyOnly: AliceLabelDto
        lateinit var labelsDtoForKeyValue: AliceLabelDto

        @BeforeAll
        @JvmStatic
        fun setup() {
            labelForKeyOnly["KeyOfLabelForKeyOnly"] = null
            labelDtoForKeyOnly = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelForKeyOnly
            )

            labelForKeyValue["KeyOfLabelForKeyValue"] = "ValueOfLabelForKeyValue"
            labelDtoForKeyValue = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelForKeyValue
            )

            labelsForKeyOnly["Key1OfLabelsForKeyOnly"] = null
            labelsForKeyOnly["Key2OfLabelsForKeyOnly"] = null
            labelsDtoForKeyOnly = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelsForKeyOnly
            )

            labelsForKeyValue["Key1OfLabelsForKeyValue"] = "Value1OfLabelsForKeyValue"
            labelsForKeyValue["Key2OfLabelsForKeyValue"] = "Value2OfLabelsForKeyValue"
            labelsDtoForKeyValue = AliceLabelDto(
                labelTarget,
                UUID.randomUUID().toString(),
                labelsForKeyValue
            )
        }
    }

    @Test
    @DisplayName("라벨 1개 저장 -  키")
    fun insertLabelOnlyKey() {
        assertEquals(labelService.setLabels(labelDtoForKeyOnly), true)
    }

    @Test
    @DisplayName("라벨 1개 저장 - 키,값 세트")
    fun insertLabelKeyValue() {
        assertEquals(labelService.setLabels(labelDtoForKeyValue), true)
    }

    @Test
    @DisplayName("라벨 1개 저장 - 중복된 키")
    fun insertDuplicateLabel() {
        assertEquals(labelService.setLabels(labelDtoForKeyOnly), true)
    }

    @Test
    @DisplayName("라벨 복수개 저장 - 키")
    fun insertLabels() {
        assertEquals(labelService.setLabels(labelsDtoForKeyOnly), true)
    }

    @Test
    @DisplayName("라벨 복수개 저장 - 키,값 세트")
    fun insertLabelsForKeyValue() {
        assertEquals(labelService.setLabels(labelsDtoForKeyValue), true)
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 가져오기")
    fun getLabel() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("특정 컴포넌트의 모든 라벨 가져오기")
    fun getLabels() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 업데이트 - 값 업데이트하기")
    fun updateLabel() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 복수개 업데이트 - 값 업데이트하기")
    fun updateLabels() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 업데이트 - 없는 키,값을 업데이트 시도하기 😱")
    fun updateNoExistLabel() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 삭제")
    fun deleteLabel() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("특정 컴포넌트의 라벨 전체 삭제하기")
    fun deleteLabels() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("없는 라벨 삭제 시도하기")
    fun deleteNoExistLabel() {
        assertThat(labelService.getLabels("", "", "").isEmpty())
    }
}