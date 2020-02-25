package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.TokenDto
import co.brainz.itsm.provider.dto.TokenInstanceDto
import co.brainz.itsm.provider.dto.TokenProcessDto
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
        val tokenInstanceDto = mapper.convertValue(result?.get("instance"), TokenInstanceDto::class.java)
        val tokenProcessDto = mapper.convertValue(result?.get("process"), TokenProcessDto::class.java)
        val tokenDto = mapper.convertValue(result?.get("token"), TokenDto::class.java)
        return TokenSaveDto(
                instanceDto = tokenInstanceDto,
                processDto = tokenProcessDto,
                tokenDto = tokenDto
        )
    }

    /**
     * Token Save.
     */
    fun postToken(jsonValue: String): Boolean {
        val tokenSaveDto = makeTokenData(jsonValue)
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.POST_TOKEN.url))
        val responseJson = restTemplate.postForEntity(url, tokenSaveDto, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    /**
     * Token Update.
     *
     * @param jsonValue
     * @return Boolean
     */
    fun putToken(jsonValue: String): Boolean {
        val tokenSaveDto = makeTokenData(jsonValue)
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Token.PUT_TOKEN.url.replace(keyRegex, tokenSaveDto.tokenDto.id)))
        val requestEntity = setHttpEntity(tokenSaveDto)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }
}
