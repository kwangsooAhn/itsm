package co.brainz.workflow.token.dto

import co.brainz.workflow.document.dto.DocumentDto
import java.io.Serializable

data class TokenSaveDto(
        val documentDto: DocumentDto,
        val tokenDto: TokenDto
) : Serializable
