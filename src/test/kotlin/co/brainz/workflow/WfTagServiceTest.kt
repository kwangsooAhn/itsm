/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.tag.service.WfTagService
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
class WfTagServiceTest {

    @Autowired
    private lateinit var wfTagService: WfTagService

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
        val tagDto = RestTemplateTagDto(
            instanceId = this.instanceId,
            tagId = "",
            tagContent = this.tagContent
        )
        assumeTrue(wfTagService.insertTag(tagDto))
    }

    @Test
    @DisplayName("Instance Tag 삭제")
    @Order(2)
    fun deleteTag() {
        val instanceDto = wfInstanceService.instance(this.instanceId)
        assumingThat(
            instanceDto.instanceId.isNotEmpty()
        ) {
            val instanceTagList = wfTagService.getInstanceTags(instanceDto.instanceId)
            for (instanceTag in instanceTagList) {
                instanceTag.value
            }
            val tagDto = instanceTagList.filter {
                it.value == this.tagContent
            }[0]
            assertEquals(tagDto.value, this.tagContent)
        }
    }
}
