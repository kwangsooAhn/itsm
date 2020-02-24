package co.brainz.workflow.token.dto

import java.io.Serializable

data class TokenSaveDto(
        val instanceDto: TokenInstanceDto,
        val processDto: TokenProcessDto,
        val tokenDto: TokenDto
        //val tokenDataDtoList: List<TokenDataDto>
) : Serializable
