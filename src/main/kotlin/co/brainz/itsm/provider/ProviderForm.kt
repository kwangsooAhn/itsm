package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.FormDto
import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class ProviderForm(private val restTemplate: RestTemplate): ProviderUtilities() {

    fun wfGetFormList(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.GET_FORM_LIST.url, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfGetForm(formId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.GET_FORM.url.replace(keyRegex, formId)))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfPostForm(formDto: FormDto): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.POST_FORM.url))
        val responseJson = restTemplate.postForEntity(url, formDto, String::class.java)
        return when (responseJson.statusCode) {
            HttpStatus.OK -> responseJson.body.toString()
            else -> ""
        }
    }

    fun wfPutForm(formDto: FormDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.PUT_FORM.url.replace(keyRegex, formDto.formId)))
        val requestEntity = setHttpEntity(formDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    fun wfDeleteForm(formId: String): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.DELETE_FORM.url.replace(keyRegex, formId)))
        val requestEntity = HttpEntity(null, null)
        val responseJson = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

}
