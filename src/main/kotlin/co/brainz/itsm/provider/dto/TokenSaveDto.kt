package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenSaveDto(
        val instanceDto: TokenInstanceDto,
        val processDto: TokenProcessDto,
        val tokenDto: TokenDto
) : Serializable
