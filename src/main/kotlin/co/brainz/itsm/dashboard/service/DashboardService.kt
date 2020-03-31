package co.brainz.itsm.dashboard.service

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class DashboardService(private val restTemplate: RestTemplateProvider) {

    /**
     * 신청한 문서 현황 count 조회
     *
     * @return
     */
   fun getStatusCountList(params: LinkedMultiValueMap<String, String>): List<RestTemplateInstanceCountDto> {
        var url =  RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES_COUNT.url, parameters = params)
        val responseBody = restTemplate.get(url)
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceCountDto::class.java))
    }
}