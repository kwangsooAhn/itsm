package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class ProviderProcess(private val restTemplate: RestTemplate): ProviderUtilities() {

    fun wfGetProcessList(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESS_LIST.url, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfGetProcess(processId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESS.url.replace(keyRegex, processId)))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfPostProcess(processDto: ProcessDto): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.POST_PROCESS.url))
        val responseJson = restTemplate.postForEntity(url, processDto, String::class.java)
        return when (responseJson.statusCode) {
            HttpStatus.OK -> responseJson.body.toString()
            else -> ""
        }
    }

    fun wfPutProcess(processDto: ProcessDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.PUT_PROCESS.url.replace(keyRegex, processDto.processId)))
        val requestEntity = setHttpEntity(processDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    fun wfDeleteProcess(processId: String): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.DELETE_PROCESS.url.replace(keyRegex, processId)))
        val requestEntity = HttpEntity(null, null)
        val responseJson = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

}
