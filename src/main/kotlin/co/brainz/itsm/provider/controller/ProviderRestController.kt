package co.brainz.itsm.provider.controller

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/provider")
class ProviderRestController(private val restTemplate: RestTemplateProvider) {

    @GetMapping("/tokens/{tokenId}")
    fun getToken(@PathVariable tokenId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        return restTemplate.get(url)
    }

    @GetMapping("/tokens/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        return restTemplate.get(url)
    }
}
