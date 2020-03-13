package co.brainz.itsm.provider.controller

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateActionDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.google.gson.Gson
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/provider")
class ProviderRestController(private val restTemplate: RestTemplateProvider) {

    @GetMapping("/tokens/{tokenId}")
    fun getToken(@PathVariable tokenId: String): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKEN.url.replace(restTemplate.getKeyRegex(), tokenId))
        return restTemplate.get(url)
    }

    @GetMapping("/tokens/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(restTemplate.getKeyRegex(), tokenId))
        return restTemplate.get(url)
    }

    @GetMapping("/tokens")
    fun getTokens(): String {
        val params = LinkedMultiValueMap<String, String>()
        params["assignee"] = "40288ab26fa3219e016fa32231230000"
        params["tokenStatus"] = "running"
        params["assigneeType"] = "user"

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKENS.url, parameters = params)
        return restTemplate.get(url)

    }

    @GetMapping("/post")
    fun postData() {
        val dataList: MutableList<RestTemplateTokenDataDto> = mutableListOf()
        val tokenDataDto1 = RestTemplateTokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb090004",
                value = "xxxxxx"
        )
        val tokenDataDto2 = RestTemplateTokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb0c0005",
                value = "ffffffffffffff"
        )
        dataList.add(tokenDataDto1)
        dataList.add(tokenDataDto2)

        val actionList = mutableListOf<RestTemplateActionDto>()
        val tokenDto = RestTemplateTokenDto(
                tokenId = "",
                documentId = "32ds1261420w7edbcd5251d7b24a6c23",
                documentName = "aaaaaaaa",
                isComplete = true,
                data = dataList,
                action = actionList
        )

        val result = LinkedHashMap<String, Any>()
        result["token"] = tokenDto

        val jsonValue = Gson().toJson(result)

        val restTemplateTokenDto = restTemplate.makeTokenData(jsonValue)
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN_DATA.url)
        restTemplate.create(url, restTemplateTokenDto)
    }

    @GetMapping("/put")
    fun putData() {
        val dataList: MutableList<RestTemplateTokenDataDto> = mutableListOf()
        val tokenDataDto1 = RestTemplateTokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb090004",
                value = "123123123123"
        )
        val tokenDataDto2 = RestTemplateTokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb0c0005",
                value = "kkkkkkkk"
        )
        dataList.add(tokenDataDto1)
        dataList.add(tokenDataDto2)

        val actionList = mutableListOf<RestTemplateActionDto>()
        val tokenDto = RestTemplateTokenDto(
                tokenId = "40288ab2707b109b01707b1103ca0001",
                documentName = "aaaaaaaa",
                isComplete = true,
                assigneeId = "aaaa",
                assigneeType = "bbbbbb",
                data = dataList,
                action = actionList
        )

        val result = LinkedHashMap<String, Any>()
        result["token"] = tokenDto

        val jsonValue = Gson().toJson(result)

        val restTemplateTokenDto = restTemplate.makeTokenData(jsonValue)
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.PUT_TOKEN_DATA.url.replace(restTemplate.getKeyRegex(), restTemplateTokenDto.tokenId))
        restTemplate.update(url, restTemplateTokenDto)
    }
}
