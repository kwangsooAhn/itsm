/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.itsm.instance.dto.InstanceCommentDto
import co.brainz.itsm.instance.service.InstanceService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Assumptions.assumingThat
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
 * 코멘트를 등록하기 위해서는 instance가 필요하다.
 */
@Disabled
@SpringBootTest
@DisplayName("Comment API 호출 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WfCommentServiceTest {

    @Autowired
    private lateinit var instanceService: InstanceService

    lateinit var userKey: String
    lateinit var instanceId: String
    lateinit var commentContent: String

    @BeforeEach
    fun init() {
        this.userKey = "0509e09412534a6e98f04ca79abb6424" // admin (기본 제공)
        // TODO : 추후 신규 instance를 추가하는 작업 진행
        this.instanceId = "4028b26774af76bb0174af84a63d000a"
        this.commentContent = "Comment Test 1"
    }

    @Test
    @DisplayName("Comment 등록")
    @Order(1)
    fun a_insertComment() {
        val commentDto = InstanceCommentDto(
            instanceId = this.instanceId,
            commentId = "",
            content = this.commentContent,
            createUserKey = this.userKey
        )
        assumeTrue(instanceService.setComment(commentDto))
    }

    @Test
    @DisplayName("Comment 목록 조회")
    @Order(2)
    fun b_getComment() {
        val comments = instanceService.getComments(this.instanceId)
        assumeTrue(!comments.isNullOrEmpty())
        assumingThat(
            comments.isNotEmpty()
        ) { assertEquals(comments[0].createUserKey, this.userKey) }
    }

    @Test
    @DisplayName("Comment 삭제")
    @Order(3)
    fun c_deleteComment() {
        val comments = instanceService.getComments(this.instanceId)
        var commentId = ""
        comments.forEach { comment ->
            if (comment.content == this.commentContent) {
                commentId = comment.commentId.toString()
            }
        }
        assumingThat(
            commentId.isNotEmpty()
        ) { assumeTrue(instanceService.deleteComment(this.instanceId, commentId)) }
    }
}
