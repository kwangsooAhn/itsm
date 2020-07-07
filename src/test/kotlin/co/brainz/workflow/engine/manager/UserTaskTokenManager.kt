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

    @Before
    fun init() {
        initTestData.setUsers(null)
        initTestData.setNumberings(null)
        initTestData.setForms(null)
        initTestData.setProcesses(null, null)
        initTestData.setDocuments(null, null, null, null, null)
        initTestData.setInstance(null, null, null, null)

        this.tokenManager = WfTokenManagerFactory(wfTokenManagerService)
            .createTokenManager(WfElementConstants.ElementType.USER_TASK.value)
    }

    @Test
    fun userTaskTokenManager_createToken_normalSave() {
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

    @Test
    fun userTaskTokenManager_completeToken_normal() {
        this.userTaskTokenManager_createToken_normalSave()
        this.tokenDto = this.tokenManager.completeToken(this.tokenDto)
        val token = wfTokenManagerService.getToken(this.tokenDto.tokenId)
        Assertions.assertThat(token.tokenStatus).isEqualTo(WfTokenConstants.Status.FINISH.code)
        Assertions.assertThat(token.tokenEndDt).isNotNull()
    }

    @Test
    fun userTaskTokenManager_createNextToken_nextUserTask() {
        this.userTaskTokenManager_createToken_normalSave()
        this.userTaskTokenManager_completeToken_normal()
        val nextTokenDto = this.tokenManager.createNextToken(this.tokenDto)!!
        Assertions.assertThat(this.tokenDto.elementId).isNotEqualTo(nextTokenDto.elementId)
        Assertions.assertThat(this.tokenDto.tokenId).isNotEqualTo(nextTokenDto.tokenId)
        Assertions.assertThat(nextTokenDto.tokenId).isNotBlank()
    }

    @Test
    fun userTaskTokenManager_createToken_assignee() {
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

    @Test
    fun userTaskTokenManager_createToken_noAssignee() {
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

    @Test
    fun userTaskTokenManager_createToken_assigneeUsers() {
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

    @Test
    fun userTaskTokenManager_createToken_notification() {
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

    @Test
    fun userTaskTokenManager_createToken_noNotification() {
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

    @Test
    fun userTaskTokenManager_createToken_statusWaiting() {
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
