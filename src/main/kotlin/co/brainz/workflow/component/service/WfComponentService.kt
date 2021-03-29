/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.service

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.provider.dto.RestTemplateFormComponentDataDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service

@Service
class WfComponentService(
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository
) {

    /**
     * 컴포넌트 데이터 목록 중 커스텀코드를 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    fun getComponentDataCustomCodeIds(parameters: LinkedHashMap<String, Any>?): List<String> {
        val customCodeIds = mutableSetOf<String>()
        val componentDataList = this.getComponentData(parameters)
        componentDataList.forEach {
            val component: MutableMap<String, Any> = ObjectMapper().readValue(it.attributeValue)
            val data: Any? = component[CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE]
            data?.let { customCodeIds.add(data.toString()) }
        }
        return customCodeIds.toList()
    }

    fun getComponentTypeById(componentId: String): String {
        val component = wfComponentRepository.findById(componentId)
        return component.get().componentType
    }

    /**
     * 컴포넌트 데이터 목록을 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    private fun getComponentData(parameters: LinkedHashMap<String, Any>?): List<RestTemplateFormComponentDataDto> {
        var componentType: String? = null
        var componentAttribute: String? = null
        if (parameters != null) {
            if (parameters["componentType"] != null) {
                componentType = parameters["componentType"].toString()
            }
            if (parameters["componentAttribute"] != null) {
                componentAttribute = parameters["componentAttribute"].toString()
            }
        }
        return wfComponentDataRepository.findComponentTypeAndAttributeId(componentType, componentAttribute)
    }
}
