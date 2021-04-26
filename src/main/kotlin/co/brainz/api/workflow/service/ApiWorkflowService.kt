/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.provider.dto.RestTemplateComponentDataDto
import co.brainz.workflow.provider.dto.RestTemplateComponentDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import org.springframework.stereotype.Service

@Service
class ApiWorkflowService(
    private val documentService: DocumentService,
    private val wfComponentService: WfComponentService,
    private val apiWorkflowDataStructure: ApiWorkflowDataStructure
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getDocumentDataStructure(documentId: String): RestTemplateTokenDataUpdateDto {
        val documentData = documentService.getDocumentData(documentId)
        return apiWorkflowDataStructure.init(documentData)
    }

    fun getComponent(componentId: String): RestTemplateComponentDto {
        val component = wfComponentService.getComponent(componentId)
        val componentData = mutableListOf<RestTemplateComponentDataDto>()
        component.attributes?.forEach { attribute ->
            val jsonElement = JsonParser().parse(attribute.attributeValue)
            val attributeValue = LinkedHashMap<String, Any>()
            when (jsonElement.isJsonArray) {
                true -> {
                    attributeValue["value"] = mapper.readValue(
                        attribute.attributeValue,
                        mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java)
                    )
                }
                false -> {
                    attributeValue["value"] = mapper.readValue(attribute.attributeValue, LinkedHashMap::class.java)
                }
            }
            componentData.add(
                RestTemplateComponentDataDto(
                    attributeId = attribute.attributeId,
                    attributeValue = attributeValue["value"] ?: ""
                )
            )
        }
        return RestTemplateComponentDto(
            componentId = componentId,
            formId = component.form.formId,
            componentType = component.componentType,
            componentData = componentData
        )
    }
}
