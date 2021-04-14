/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.workflow.instance.service.WfInstanceService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Assumptions.assumingThat
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

    lateinit var tagContent: String
    lateinit var instanceId: String

    @BeforeEach
    fun init() {
        this.instanceId = "4028b26774af76bb0174af84a63d000a"
        this.tagContent = "Test Tag 1"
    }

    @Test
    @DisplayName("Instance Tag 추가")
    @Order(1)
    fun insertTag() {
        val tagDto = AliceTagDto(
            tagId = "",
            tagType = AliceTagConstants.TagType.INSTANCE.code,
            tagValue = this.tagContent,
            targetId = this.instanceId
        )
        assumeTrue(aliceTagService.insertTag(tagDto))
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
            val tagDto = instanceTagList.filter {
                it.tagValue == this.tagContent
            }[0]
            assertEquals(tagDto.tagValue, this.tagContent)

            tagDto.tagId?.let { aliceTagService.deleteTag(it) }
        }
    }
}
