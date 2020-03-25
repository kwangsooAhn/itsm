package co.brainz.itsm.dashboard.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class DashboardService(private val restTemplate: RestTemplateProvider) {

    /**
     * 신청한 문서 현황 count 조회
     *
     * @return
     */
   fun getStatusCountList(): LinkedMultiValueMap<String, Any> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)

        var testData =  LinkedMultiValueMap<String, Any>()
        testData["running"] = 100
        testData["wait"] = 22
        testData["finish"] = 0

        return testData
    }
}