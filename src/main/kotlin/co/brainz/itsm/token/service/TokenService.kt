/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.service.DocumentActionService
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.service.WfTokenService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val documentActionService: DocumentActionService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val wfComponentService: WfComponentService,
    private val aliceFileService: AliceFileService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfEngine: WfEngine
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            instanceId = restTemplateTokenDataUpdateDto.instanceId,
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            instanceCreateUser = restTemplateTokenDataUpdateDto.assigneeId?.let { AliceUserEntity(userKey = it) },
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = RestTemplateConstants.InstancePlatform.ITSM.code
        )

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) ==
                    (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.startWorkflow(wfEngine.toTokenDto(tokenDto))
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun putToken(tokenId: String, restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = RestTemplateConstants.InstancePlatform.ITSM.code
        )

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) ==
                    (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.progressWorkflow(wfEngine.toTokenDto(tokenDto))
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param tokenSearchCondition
     * @return List<tokenDto>
     */
    fun getTokenList(
        tokenSearchCondition: TokenSearchCondition
    ): RestTemplateInstanceListReturnDto {
        tokenSearchCondition.userKey = currentSessionUser.getUserKey()
        return wfInstanceService.instances(tokenSearchCondition)
    }

    /**
     * [tokenId]를 받아서 처리할 문서 상세 조회 하여 [String]반환 한다.
     */
    fun findToken(tokenId: String): String =
        documentActionService.makeTokenAction(mapper.writeValueAsString(wfTokenService.getTokenData(tokenId)))

    fun getTodoTokenCount(): Long = getTokenList(
        TokenSearchCondition(
            userKey = currentSessionUser.getUserKey(),
            searchTokenType = WfTokenConstants.SearchType.TODO.code
        )
    ).totalCount

    /**
     * 해당 인스턴스를 가진 토큰 데이터를 조회한다.
     */
    fun findTokenByInstanceId(instanceId: String): List<WfTokenDto> {
        return wfTokenService.findTokenByInstanceId(instanceId)
    }
}
