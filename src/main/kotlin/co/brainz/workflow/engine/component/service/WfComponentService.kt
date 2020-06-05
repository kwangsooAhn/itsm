package co.brainz.workflow.engine.component.service

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
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
     * 컴포넌트 데이터 목록을 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    fun getComponentData(parameters: LinkedHashMap<String, Any>?): List<RestTemplateFormComponentDataDto> {
        // 중복제거를 위해 Set 타입으로 변수 사용.
        val componentDataList = mutableListOf<RestTemplateFormComponentDataDto>()

        val componentType = parameters?.get("componentType")
        val componentAttribute = parameters?.get("componentAttribute")

        val componentDataEntities = if (componentType != null && componentAttribute != null) {
            wfComponentDataRepository.findAllByComponentTypeAndAttributeId(componentType, componentAttribute)
        } else {
            wfComponentDataRepository.findAll()
        }
        componentDataEntities.forEach {
            componentDataList.add(
                RestTemplateFormComponentDataDto(
                    componentId = it.componentId,
                    attributeId = it.attributeId,
                    attributeValue = it.attributeValue
                )
            )
        }

        return componentDataList
    }

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
        return customCodeIds.toMutableList()
    }

    fun getComponentTypeById(componentId: String): String {
        val component = wfComponentRepository.findById(componentId)
        return component.get().componentType
    }
}
