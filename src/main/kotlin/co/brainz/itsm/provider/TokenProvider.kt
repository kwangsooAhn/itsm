package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.TokenDataDto
import co.brainz.itsm.provider.dto.TokenDto
import co.brainz.itsm.provider.dto.UrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TokenProvider(private val restTemplate: RestTemplate): ProviderUtilities() {

    /**
     * Token Data Converter (String -> Dto).
     *
     * @param jsonValue
     * @return TokenDto
     */
    fun makeTokenData(jsonValue: String): TokenDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val result: MutableMap<*, *>? = mapper.readValue(jsonValue, MutableMap::class.java)
        val tokenMap = mapper.convertValue(result?.get("token"), Map::class.java)
        val tokenDataList: MutableList<TokenDataDto> = mapper.convertValue(tokenMap["data"], mapper.typeFactory.constructCollectionType(MutableList::class.java, TokenDataDto::class.java))
        return TokenDto(
                tokenId = tokenMap["tokenId"] as String,
                documentId = tokenMap["documentId"] as String,
                elementId = tokenMap["elementId"] as String,
                isComplete = tokenMap["isComplete"] as Boolean,
                assigneeId = tokenMap["assigneeId"]?.toString(),
                assigneeType = tokenMap["assigneeType"]?.toString(),
                data = tokenDataList
        )
    }

    /**
     * Token Save.
     *
     * @param tokenDto
     * @return Boolean
     */
    fun postTokenData(tokenDto: TokenDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.POST_TOKEN_DATA.url))
        val responseJson = restTemplate.postForEntity(url, tokenDto, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    /**
     * Token Update.
     *
     * @param tokenDto
     * @return Boolean
     */
    fun putTokenData(tokenDto: TokenDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.PUT_TOKEN_DATA.url.replace(keyRegex, tokenDto.tokenId)))
        val requestEntity = setHttpEntity(tokenDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }
}
