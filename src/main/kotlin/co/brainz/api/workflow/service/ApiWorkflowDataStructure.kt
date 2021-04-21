/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.framework.util.AliceUtil
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import org.springframework.stereotype.Service

@Service
class ApiWorkflowDataStructure {

    /**
     * 기본적인 데이터 구조 설정
     */
    fun init(documentData: RestTemplateRequestDocumentDto): RestTemplateTokenDataUpdateDto {
        val dataStructure = RestTemplateTokenDataUpdateDto(
            documentId = documentData.documentId,
            instanceId = AliceUtil().getUUID(),
            action = WfElementConstants.Action.PROGRESS.value
        )
        val componentData = mutableListOf<RestTemplateTokenDataDto>()
        val filterType: Array<String> = this.getFilterType()
        for (component in documentData.form.components) {
            if (!filterType.contains(component.type)) {
                componentData.add(
                    RestTemplateTokenDataDto(
                        componentId = component.componentId
                    )
                )
            }
        }

        if (componentData.isNotEmpty()) {
            dataStructure.componentData = componentData
        }

        return dataStructure
    }

    /**
     * 데이터가 없는 예외 컴포넌트 타입 목록 조회 (해당 컴포넌트는 제외)
     */
    private fun getFilterType(): Array<String> {
        return arrayOf(
            WfComponentConstants.ComponentTypeCode.DIVIDER.code,
            WfComponentConstants.ComponentTypeCode.LABEL.code,
            WfComponentConstants.ComponentTypeCode.ACCORDION_START.code,
            WfComponentConstants.ComponentTypeCode.ACCORDION_END.code
        )
    }
}
