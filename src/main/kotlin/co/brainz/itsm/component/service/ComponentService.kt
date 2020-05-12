package co.brainz.itsm.component.service

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class ComponentService(private val restTemplate: RestTemplateProvider) {

    /**
     * 컴포넌트 데이터 목록을 조회하여 리턴한다.
     * 파라미터[parameters]에 따라 조회 결과를 필터한다.
     */
    fun getComponentDataCustomCodeIds(parameters: LinkedMultiValueMap<String, String>): List<String> {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Component.GET_COMPONENT_CUSTOM_CODE_IDS.url,
            parameters = parameters
        )
        val responseBody = restTemplate.get(urlDto)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, String::class.java)
        )
    }
}
