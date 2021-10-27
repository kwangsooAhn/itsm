/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import org.springframework.stereotype.Service

@Service
class ApiWorkflowDataStructure {

    private val apiExcludedComponentType: Array<String> = arrayOf(
        WfComponentConstants.ComponentTypeCode.DIVIDER.code,
        WfComponentConstants.ComponentTypeCode.LABEL.code
    )

    /**
     * 기본적인 데이터 구조 설정
     */
    fun init(documentData: RestTemplateRequestDocumentDto): RestTemplateTokenDataUpdateDto {
        val dataStructure = RestTemplateTokenDataUpdateDto(
            documentId = documentData.documentId,
            action = WfElementConstants.Action.PROGRESS.value
        )
        val componentData = mutableListOf<RestTemplateTokenDataDto>()
        documentData.form.group?.let {
            for (group in documentData.form.group) {
                for (row in group.row) {
                    for (component in row.component) {
                        if (!apiExcludedComponentType.contains(component.type)) {
                            componentData.add(
                                RestTemplateTokenDataDto(
                                    componentId = component.id
                                )
                            )
                        }
                    }
                }
            }
        }

        if (componentData.isNotEmpty()) {
            dataStructure.componentData = componentData
        }

        return dataStructure
    }
}
