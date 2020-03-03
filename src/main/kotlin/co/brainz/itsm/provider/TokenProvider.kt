package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.DocumentDto
import co.brainz.itsm.provider.dto.TokenDto
import co.brainz.itsm.provider.dto.TokenDataDto
import co.brainz.itsm.provider.dto.TokenSaveDto
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
     * @return TokenSaveDto
     */
    fun makeTokenData(jsonValue: String): TokenSaveDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val result: MutableMap<*, *>? = mapper.readValue(jsonValue, MutableMap::class.java)
        val documentMap = mapper.convertValue(result?.get("document"), Map::class.java)
        val tokenMap = mapper.convertValue(result?.get("token"), Map::class.java)
        val documentDto = DocumentDto(documentId = documentMap["id"] as String)
        val tokenDataList: MutableList<TokenDataDto> = mapper.convertValue(tokenMap["data"], mapper.typeFactory.constructCollectionType(MutableList::class.java, TokenDataDto::class.java))
        val tokenDto = TokenDto(
                tokenId = tokenMap["id"] as String,
                elementId = tokenMap["elementId"] as String,
                isComplete = tokenMap["isComplete"] as Boolean,
                assigneeId = tokenMap["assigneeId"]?.toString(),
                assigneeType = tokenMap["assigneeType"]?.toString(),
                data = tokenDataList
        )
        return TokenSaveDto(
                documentDto = documentDto,
                tokenDto = tokenDto
        )
    }

    /**
     * Token Save.
     *
     * @param tokenSaveDto
     * @return Boolean
     */
    fun postToken(tokenSaveDto: TokenSaveDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.POST_TOKEN.url))
        val responseJson = restTemplate.postForEntity(url, tokenSaveDto, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    /**
     * Token Update.
     *
     * @param tokenSaveDto
     * @return Boolean
     */
    fun putToken(tokenSaveDto: TokenSaveDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.PUT_TOKEN.url.replace(keyRegex, tokenSaveDto.tokenDto.tokenId)))
        val requestEntity = setHttpEntity(tokenSaveDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }
}
