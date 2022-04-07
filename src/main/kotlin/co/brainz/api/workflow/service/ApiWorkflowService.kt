/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.api.dto.RequestCmdbDto
import co.brainz.api.dto.RequestDto
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.component.dto.WfCIComponentValueDto
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.ApiComponentDto
import co.brainz.workflow.provider.dto.ComponentPropertyDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.token.repository.WfTokenDataRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ApiWorkflowService(
    private val documentService: DocumentService,
    private val wfInstanceService: WfInstanceService,
    private val wfEngine: WfEngine,
    private val wfComponentService: WfComponentService,
    private val apiWorkflowMapper: ApiWorkflowMapper,
    private val aliceMessageSource: AliceMessageSource,
    private val ciService: CIService,
    private val tokenService: TokenService,
    private val wfTokenDataRepository: WfTokenDataRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getDocumentDataStructure(documentId: String): RestTemplateRequestDocumentDto {
        return documentService.getDocumentData(documentId)
    }

    fun getComponent(componentId: String): ApiComponentDto {
        val component = wfComponentService.getComponent(componentId)
        val properties = mutableListOf<ComponentPropertyDto>()
        component.properties?.forEach { property ->
            properties.add(
                ComponentPropertyDto(
                    type = property.propertyType,
                    options = mapper.readValue(property.propertyOptions, LinkedHashMap::class.java)
                )
            )
        }
        return ApiComponentDto(
            componentId = componentId,
            componentType = component.componentType,
            mappingId = component.mappingId,
            formId = component.form.formId,
            formRowId = component.formRow?.formRowId ?: "",
            properties = properties
        )
    }

    fun callDocument(documentId: String, requestDto: RequestDto): Boolean {
        val documentDto = documentService.getDocument(documentId)
        when (documentDto.apiEnable) {
            true -> {
                val tokenDto = apiWorkflowMapper.callDataMapper(documentId, requestDto)
                tokenDto.instancePlatform = WorkflowConstants.InstancePlatform.API.code
                return wfEngine.startWorkflow(wfEngine.toTokenDto(tokenDto))
            }
            false -> {
                throw AliceException(
                    AliceErrorConstants.ERR_00003,
                    aliceMessageSource.getMessage("auth.msg.accessDenied")
                )
            }
        }
    }

    fun getInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        return wfInstanceService.getInstancesHistory(instanceId)
    }

    @Transactional
    fun callCmdbDocument(requestCmdbList: List<RequestCmdbDto>): Boolean {
        requestCmdbList.forEach { requestCmdb ->
            val instanceId = AliceUtil().getUUID()

            // CI 임시 등록 (wf_component_ci_data)
            requestCmdb.ciComponentData.forEach {
                it.instanceId = instanceId
                ciService.saveCIComponentData(it.ciId, it)
            }
            // 컴포넌트별 값 설정 (기본값 + CI)
            val tokenDataList = mutableListOf<RestTemplateTokenDataDto>()
            tokenDataList.addAll(requestCmdb.default)

            val ciValueList = mutableListOf<WfCIComponentValueDto>()
            requestCmdb.ciData.forEach { ci ->
                ciValueList.add(
                    WfCIComponentValueDto(
                        ciId = ci.ciId,
                        ciNo = ci.ciNo,
                        typeId = ci.typeId,
                        ciName = ci.ciName,
                        ciDesc = ci.ciDesc,
                        ciStatus = ci.ciStatus,
                        interlink = ci.interlink,
                        actionType = ci.actionType
                    )
                )
            }
            tokenDataList.add(
                RestTemplateTokenDataDto(
                    componentId = requestCmdb.targetComponentId,
                    value = mapper.writeValueAsString(ciValueList)
                )
            )
            tokenService.componentDataConverter(tokenDataList)

            // 신청서 등록 (WfEngine)
            val requestDto = RequestDto(
                documentId = requestCmdb.documentId,
                instanceId = instanceId,
                assigneeId = requestCmdb.assigneeId,
                action = WfElementConstants.Action.PROGRESS.value,
                componentData = tokenDataList
            )
            this.callDocument(requestCmdb.documentId, requestDto)
        }
        return true
    }

    /**
     * 문서를 다음 단계로 진행
     */
    fun callWorkflow(documentNo: String): Boolean {
        val instance = wfInstanceService.getInstanceListInDocumentNo(documentNo).first()
        val token = wfInstanceService.getInstanceLatestToken(instance.instanceId)
        // 현재 토큰 처리자는 시스템으로 고정
        token.assigneeId = AliceUserConstants.CREATE_USER_ID
        val tokenDto = WfTokenDto(
            tokenId = token.tokenId,
            documentId = instance.document.documentId,
            documentName = instance.document.documentName,
            instanceId = instance.instanceId,
            elementId = token.elementId,
            elementType = token.elementType,
            tokenStatus = token.tokenStatus,
            tokenAction = token.action,
            assigneeId = token.assigneeId,
            numberingId = token.numberingId,
            parentTokenId = token.parentTokenId,
            instanceCreateUser = token.instanceCreateUser,
            action = WfElementConstants.Action.PROGRESS.value,
            instancePlatform = WorkflowConstants.InstancePlatform.API.code,
            data = wfTokenDataRepository.getTokenDataList(token.tokenId)
        )
        return wfEngine.progressWorkflow(tokenDto)
    }
}
