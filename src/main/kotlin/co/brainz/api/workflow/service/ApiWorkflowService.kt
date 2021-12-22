/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.api.dto.RequestDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.ApiComponentDto
import co.brainz.workflow.provider.dto.ComponentPropertyDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class ApiWorkflowService(
    private val documentService: DocumentService,
    private val instanceService: WfInstanceService,
    private val wfEngine: WfEngine,
    private val wfComponentService: WfComponentService,
    private val apiWorkflowMapper: ApiWorkflowMapper,
    private val aliceMessageSource: AliceMessageSource
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
                // TODO : CMDB 와 같이 별도의 저장 옵션이 존재하는 경우 해당 값을 추가하여 처리한다.
                // if (requestDto.optionData != null) { }

                val tokenDto = apiWorkflowMapper.callDataMapper(documentId, requestDto)
                tokenDto.instancePlatform = RestTemplateConstants.InstancePlatform.API.code
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
        return instanceService.getInstancesHistory(instanceId)
    }
}
