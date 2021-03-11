package co.brainz.itsm.component.service

import co.brainz.workflow.component.service.WfComponentService
import org.springframework.stereotype.Service

@Service
class ComponentService(
    private val wfComponentService: WfComponentService
) {

    /**
     * 컴포넌트 데이터 목록을 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    fun getComponentDataCustomCodeIds(parameters: LinkedHashMap<String, Any>?): List<String> {
        return wfComponentService.getComponentDataCustomCodeIds(parameters)
    }
}
