/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import javax.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserTaskTokenManager {

    @Autowired
    lateinit var initTestData: InitTestData

    @Autowired
    lateinit var wfElementRepository: WfElementRepository

    @Autowired
    lateinit var wfElementDataRepository: WfElementDataRepository

    @Autowired
    lateinit var wfTokenManagerService: WfTokenManagerService

    lateinit var tokenManager: WfTokenManager

    private var tokenDto: WfTokenDto = WfTokenDto()

    /**
     * 초기 데이터 셋팅.
     * 데이터 설정 후 initTestData.getData() 로 등록한 데이터를 가져올 수 있음.
     * 추가 설정은 [InitTestData] 에 구현 후 호출.
     */
    @Before
    fun init() {
        initTestData.setUsers()
        initTestData.setNumberings()
        initTestData.setForms()
        initTestData.setProcesses()
        initTestData.setDocuments()
        initTestData.setInstance()

        this.tokenManager = WfTokenManagerFactory(wfTokenManagerService)
            .createTokenManager(WfElementConstants.ElementType.USER_TASK.value)
    }

    /**
     * 토큰 생성 (기본).
     */
    @Test
    fun userTaskTokenManager_createToken_default_OK() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.tokenId).isNotBlank()
    }

    /**
     * 토큰 종료 여부.
     */
    @Test
    fun userTaskTokenManager_completeToken_default_OK() {
        this.userTaskTokenManager_createToken_default_OK()
        this.tokenDto = this.tokenManager.completeToken(this.tokenDto)
        val token = wfTokenManagerService.getToken(this.tokenDto.tokenId)
        Assertions.assertThat(token.tokenStatus).isEqualTo(WfTokenConstants.Status.FINISH.code)
        Assertions.assertThat(token.tokenEndDt).isNotNull()
    }

    /**
     * 다음 토큰 존재 여부 확인.
     */
    @Test
    fun userTaskTokenManager_createNextToken_element_Exists() {
        this.userTaskTokenManager_createToken_default_OK()
        this.userTaskTokenManager_completeToken_default_OK()
        val nextTokenDto = this.tokenManager.createNextToken(this.tokenDto)!!
        Assertions.assertThat(this.tokenDto.elementId).isNotEqualTo(nextTokenDto.elementId)
        Assertions.assertThat(this.tokenDto.tokenId).isNotEqualTo(nextTokenDto.tokenId)
        Assertions.assertThat(nextTokenDto.tokenId).isNotBlank()
    }

    /**
     * 토큰 생성시 담당자 타입을 Assignee 으로 설정.
     */
    @Test
    fun userTaskTokenManager_createToken_assigneeType_assignee_Exists() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        val elementEntity = wfTokenManagerService.getElement(elementId)
        val copyElementEntity = elementEntity.copy(notification = true)
        wfElementRepository.save(copyElementEntity)

        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId,
            assigneeId = this.initTestData.getData().users!![0].userKey
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.assigneeId).isNotBlank()
    }

    /**
     * 토큰 생성시 담당자를 빈값으로 처리할 경우.
     */
    @Test
    fun userTaskTokenManager_createToken_assigneeId_Empty() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId,
            assigneeId = ""
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.assigneeId).isBlank()
    }

    /**
     * 토큰 생성시 담당자 타입을 사용자로 설정할 경우 후보자 설정에 데이터가 존재하는지 확인.
     */
    @Test
    fun userTaskTokenManager_createToken_assigneeType_assigneeUsers_Exists() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        val element = wfElementRepository.findWfElementEntityByElementId(elementId)
        val elementDataEntity = wfElementDataRepository.findByElementAndAttributeId(element, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
        val copyElementDataEntity = elementDataEntity.copy(attributeValue = WfTokenConstants.AssigneeType.USERS.code)
        wfElementDataRepository.save(copyElementDataEntity)
        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId,
            assigneeId = ""
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)

        Assertions.assertThat(token.candidate).size().isNotZero
    }

    /**
     * 토큰 생성시 알림 기능 ON.
     */
    @Test
    fun userTaskTokenManager_createToken_notification_TRUE() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        val elementEntity = wfTokenManagerService.getElement(elementId)
        val copyElementEntity = elementEntity.copy(notification = true)
        wfElementRepository.save(copyElementEntity)

        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.element.notification).isTrue()
    }

    /**
     * 토큰 생성시 알림 기능 OFF.
     */
    @Test
    fun userTaskTokenManager_createToken_notification_FALSE() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        val elementEntity = wfTokenManagerService.getElement(elementId)
        val copyElementEntity = elementEntity.copy(notification = false)
        wfElementRepository.save(copyElementEntity)

        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.element.notification).isFalse()
    }

    /**
     * 토큰 생성시 토큰 상태를 Waiting 으로 설정.
     */
    @Test
    fun userTaskTokenManager_createToken_status_Waiting() {
        var elementId = ""
        run loop@{
            initTestData.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = initTestData.getData().documents!![0].documentId,
            instanceId = initTestData.getData().instance!!.instanceId,
            instanceCreateUser = initTestData.getData().instance!!.instanceCreateUser,
            elementId = elementId,
            tokenStatus = WfTokenConstants.Status.WAITING.code
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val method = WfTokenManager::class.java.getDeclaredMethod("suspendToken", WfTokenDto::class.java)
        method.isAccessible = true
        method.invoke(this.tokenManager, this.tokenDto)

        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.tokenStatus).isEqualTo(WfTokenConstants.Status.WAITING.code)
    }
}
