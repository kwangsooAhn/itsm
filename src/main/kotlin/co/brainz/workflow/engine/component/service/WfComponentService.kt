package co.brainz.workflow.engine.component.service

import co.brainz.workflow.engine.component.mapper.WfComponentDataMapper
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfComponentService(
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository
) {

    private val componentDataMapper = Mappers.getMapper(WfComponentDataMapper::class.java)

    /**
     * 컴포넌트 데이터 목록을 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    fun getComponentData(parameters: LinkedHashMap<String, Any>?): List<WfFormComponentDataDto> {
        val componentDataList = mutableListOf<WfFormComponentDataDto>()

        val componentType = parameters?.get("componentType")
        val componentAttribute = parameters?.get("componentAttribute")

        // 모든 컴포넌트 데이터의 컴포넌트 타입이 커스텀코드인 display 데이터를 조회한다.
        val componentDataEntities = if (componentType != null && componentAttribute != null) {
            wfComponentDataRepository.findAllCustomCodeDisplayAttr(componentType, componentAttribute)
        } else {
            wfComponentDataRepository.findAll()
        }
        componentDataEntities.forEach {
            componentDataList.add(componentDataMapper.toComponentDataDto(it))
        }

        return componentDataList
    }
}
