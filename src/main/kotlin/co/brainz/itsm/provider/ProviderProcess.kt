package co.brainz.itsm.provider

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.itsm.provider.dto.UrlDto
import co.brainz.workflow.process.dto.WfProcessElementDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class ProviderProcess(private val restTemplate: RestTemplate): ProviderUtilities() {

    /**
     * Search Processes.
     *
     * @param params
     * @return String
     */
    fun getProcesses(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESSES.url, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    /**
     * Search Process.
     *
     * @param processId
     * @return String
     */
    fun getProcess(processId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESS.url.replace(keyRegex, processId)))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    /**
     * Request for new process.
     *
     * @param processDto
     * @return String
     */
    fun createProcess(processDto: ProcessDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        processDto.createDt = ProviderUtilities().toGMT(LocalDateTime.now())
        processDto.createUserKey = userDetails.userKey
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.POST_PROCESS.url))
        val responseJson = restTemplate.postForEntity(url, processDto, String::class.java)
        return when (responseJson.statusCode) {
            HttpStatus.OK -> responseJson.body.toString()
            else -> ""
        }
    }

    /**
     * Update Process.
     *
     * @param wfProcessElementDto
     * @return Boolean
     */
    fun updateProcess(wfProcessElementDto: WfProcessElementDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val processId = wfProcessElementDto.process?.id?:""
        wfProcessElementDto.process?.updateDt = ProviderUtilities().toGMT(LocalDateTime.now())
        wfProcessElementDto.process?.updateUserKey = userDetails.userKey
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.PUT_PROCESS.url.replace(keyRegex, processId)))
        val requestEntity = setHttpEntity(wfProcessElementDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    /**
     * Delete Process.
     *
     * @param processId
     * @return Boolean
     */
    fun deleteProcess(processId: String): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.DELETE_PROCESS.url.replace(keyRegex, processId)))
        val requestEntity = HttpEntity(null, null)
        val responseJson = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

}
