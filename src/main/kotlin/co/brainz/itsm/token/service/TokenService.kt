package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.provider.ProviderWorkflow
import co.brainz.itsm.provider.TokenProvider
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.InstanceViewDto
import co.brainz.itsm.provider.dto.TokenDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TokenService(private val tokenProvider: TokenProvider, private val providerWorkflow: ProviderWorkflow) {

    /**
     * Token 신규 등록 / 처리
     * isComplete : false일 경우에는 저장, true일 경우에 처리
     *
     * @return Boolean
     */
    fun createToken(tokenDto: TokenDto): Boolean {
        return tokenProvider.postTokenData(tokenDto)
    }

    /**
     * Token 수정 / 처리
     * isComplete : false일 경우에는 수정, true일 경우에 처리
     *
     * @return Boolean
     */
    fun updateToken(tokenDto: TokenDto): Boolean {
        return tokenProvider.putTokenData(tokenDto)
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @return List<tokenDto>
     */
    fun getTokenList(): List<InstanceViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("status", ProviderConstants.TokenStatus.RUNNING.value)

        val responseBody = providerWorkflow.getProcessInstances(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tokens: List<InstanceViewDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, InstanceViewDto::class.java))
        for (token in tokens) {
            token.createDt = token.createDt.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return tokens
    }
}
