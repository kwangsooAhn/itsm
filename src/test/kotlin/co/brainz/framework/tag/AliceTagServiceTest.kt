/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.workflow.instance.service.WfInstanceService
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
@DisplayName("Tag API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AliceTagServiceTest {

    @Autowired
    private lateinit var aliceTagService: AliceTagService

    @Autowired
    private lateinit var wfInstanceService: WfInstanceService

    lateinit var tagValue: String
    lateinit var instanceId: String

    @BeforeEach
    fun init() {
        this.instanceId = "4028b26774af76bb0174af84a63d000a"
        this.tagValue = "Test Tag 1"
    }

    @Test
    @DisplayName("Instance Tag 추가")
    @Order(1)
    fun insertTag() {
        val tagDto = AliceTagDto(
            tagId = "",
            tagType = AliceTagConstants.TagType.INSTANCE.code,
            tagValue = this.tagValue,
            targetId = this.instanceId
        )

        assumeTrue(aliceTagService.insertTag(tagDto) != null)
    }

    @Test
    @DisplayName("Instance Tag 삭제")
    @Order(2)
    fun deleteTag() {
        val instanceDto = wfInstanceService.instance(this.instanceId)
        assumingThat(
            instanceDto.instanceId.isNotEmpty()
        ) {
            val instanceTagList =
                aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.INSTANCE.code, instanceDto.instanceId)
            for (instanceTag in instanceTagList) {
                instanceTag.tagValue
            }
            val tempTagValue = instanceTagList.filter {
                it.tagValue == this.tagValue
            }[0]
            assertEquals(tempTagValue.tagValue, this.tagValue)

            tempTagValue.tagId?.let { aliceTagService.deleteTag(it) }
        }
    }
}
