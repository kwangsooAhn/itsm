package co.brainz.workflow.token.dto

import java.io.Serializable

data class TokenDataDto(
        val compId: String,
        var value: String
) : Serializable

