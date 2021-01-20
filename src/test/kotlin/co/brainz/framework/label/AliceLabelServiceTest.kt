/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label

/*import co.brainz.framework.label.controller.LabelRestController
import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.service.AliceLabelService
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.hasItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.HashMap*/

// Junit5로 작성중이던 테스트코드.
// 나중에 Junit5로 변환하면 이어서 만들자.
// hcjung 2021-01-19

/*@ExtendWith(SpringExtension::class)
@SpringBootTest
@Transactional
@DisplayName("라벨링 서비스 테스트")*/
class AliceLabelServiceTest {
/*
    @Autowired
    private lateinit var labelRestController: LabelRestController
    //private lateinit var labelService: AliceLabelService

    private lateinit var mvc: MockMvc

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
        assertTrue(labelRestController.setLabels(labelDtoForKeyOnly))
    }

    @Test
    @DisplayName("라벨 1개 저장 - 키,값 세트")
    fun insertLabelKeyValue() {
        assertTrue(labelRestController.setLabels(labelDtoForKeyValue))
    }

    @Test
    @DisplayName("라벨 1개 저장 - 중복된 키")
    fun insertDuplicateLabel() {
        assertTrue(labelRestController.setLabels(labelDtoForKeyOnly))
    }

    @Test
    @DisplayName("라벨 복수개 저장 - 키")
    fun insertLabels() {
        assertTrue(labelRestController.setLabels(labelsDtoForKeyOnly))
    }

    @Test
    @DisplayName("라벨 복수개 저장 - 키,값 세트")
    fun insertLabelsForKeyValue() {
        assertTrue(labelRestController.setLabels(labelsDtoForKeyValue))
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 가져오기")
    fun getLabel() {
        assertTrue(
            labelRestController.getLabels(
                labelDtoForKeyOnly.labelTarget,
                labelDtoForKeyOnly.labelTargetId,
                "KeyOfLabelForKeyOnly"
            ))
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("특정 컴포넌트의 모든 라벨 가져오기")
    fun getLabels() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 업데이트 - 값 업데이트하기")
    fun updateLabel() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 복수개 업데이트 - 값 업데이트하기")
    fun updateLabels() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 업데이트 - 없는 키,값을 업데이트 시도하기 😱")
    fun updateNoExistLabel() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("라벨 1개 삭제")
    fun deleteLabel() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("특정 컴포넌트의 라벨 전체 삭제하기")
    fun deleteLabels() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("없는 라벨 삭제 시도하기")
    fun deleteNoExistLabel() {
        assertThat(labelRestController.getLabels("", "", "").isEmpty())
    }*/
}
