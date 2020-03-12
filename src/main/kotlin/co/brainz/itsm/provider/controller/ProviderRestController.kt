package co.brainz.itsm.provider.controller

import co.brainz.itsm.provider.TokenProvider
import co.brainz.itsm.provider.dto.ActionDto
import co.brainz.itsm.provider.dto.TokenDataDto
import co.brainz.itsm.provider.dto.TokenDto
import com.google.gson.Gson
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/provider")
class ProviderRestController(private val tokenProvider: TokenProvider) {

    @GetMapping("/tokens/{tokenId}")
    fun getToken(@PathVariable tokenId: String): String {
        return tokenProvider.getToken(tokenId)
    }

    @GetMapping("/tokens/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): String {
        return tokenProvider.getTokenData(tokenId)
    }

    @GetMapping("/tokens")
    fun getTokens(): String {
        val params = LinkedMultiValueMap<String, String>()
        params["assignee"] = "40288ab26fa3219e016fa32231230000"
        params["tokenStatus"] = "running"
        params["assigneeType"] = "user"
        return tokenProvider.getTokens(params)
    }

    @GetMapping("/post")
    fun postData() {
        val dataList: MutableList<TokenDataDto> = mutableListOf()
        val tokenDataDto1 = TokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb090004",
                value = "xxxxxx"
        )
        val tokenDataDto2 = TokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb0c0005",
                value = "ffffffffffffff"
        )
        dataList.add(tokenDataDto1)
        dataList.add(tokenDataDto2)

        val actionList = mutableListOf<ActionDto>()
        val tokenDto = TokenDto(
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
        tokenProvider.postTokenData(tokenProvider.makeTokenData(jsonValue))
    }

    @GetMapping("/put")
    fun putData() {
        val dataList: MutableList<TokenDataDto> = mutableListOf()
        val tokenDataDto1 = TokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb090004",
                value = "123123123123"
        )
        val tokenDataDto2 = TokenDataDto(
                componentId = "40288ab77066ce0a0170670cbb0c0005",
                value = "kkkkkkkk"
        )
        dataList.add(tokenDataDto1)
        dataList.add(tokenDataDto2)

        val actionList = mutableListOf<ActionDto>()
        val tokenDto = TokenDto(
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
        tokenProvider.putTokenData(tokenProvider.makeTokenData(jsonValue))
    }
}
