package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenSaveDto(
        val documentDto: DocumentDto,
        val tokenDto: TokenDto
) : Serializable
