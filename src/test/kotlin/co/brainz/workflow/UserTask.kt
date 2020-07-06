/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import javax.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class UserTask {

    @Autowired
    lateinit var configuration: Configuration

    @Autowired
    lateinit var wfTokenManagerService: WfTokenManagerService

    lateinit var tokenManager: WfTokenManager

    private var tokenDto: WfTokenDto = WfTokenDto()

    @Before
    fun init() {
        configuration.setUsers(null)
        configuration.setNumbering(null)
        configuration.setForms(null)
        configuration.setProcesses(null, null)
        configuration.setDocument(null, null, null, null, null)
        configuration.setInstance(null, null, null, null)

        this.tokenManager = WfTokenManagerFactory(wfTokenManagerService)
            .createTokenManager(WfElementConstants.ElementType.USER_TASK.value)
    }

    @Test
    fun run() {
        createToken()
        completeToken()
        createNextToken()
    }

    fun createToken() {
        var elementId = ""
        run loop@{
            configuration.getData().processes!![0].elementEntities.forEach { element ->
                if (element.elementType == WfElementConstants.ElementType.USER_TASK.value) {
                    elementId = element.elementId
                    return@loop
                }
            }
        }
        this.tokenDto = WfTokenDto(
            tokenId = "",
            documentId = configuration.getData().documents!![0].documentId,
            instanceId = configuration.getData().instance!!.instanceId,
            instanceCreateUser = configuration.getData().instance!!.instanceCreateUser,
            elementId = elementId
        )
        this.tokenDto = this.tokenManager.createToken(tokenDto)
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        Assertions.assertThat(token.tokenId).isNotBlank()
    }

    fun completeToken() {
        this.tokenDto = this.tokenManager.completeToken(this.tokenDto)
        val token = wfTokenManagerService.getToken(this.tokenDto.tokenId)
        Assertions.assertThat(token.tokenStatus).isEqualTo(WfTokenConstants.Status.FINISH.code)
        Assertions.assertThat(token.tokenEndDt).isNotNull()
    }

    fun createNextToken() {
        val nextTokenDto = this.tokenManager.createNextToken(this.tokenDto)!!
        Assertions.assertThat(this.tokenDto.elementId).isNotEqualTo(nextTokenDto.elementId)
        Assertions.assertThat(this.tokenDto.tokenId).isNotEqualTo(nextTokenDto.tokenId)
        Assertions.assertThat(nextTokenDto.tokenId).isNotBlank()
    }
}
