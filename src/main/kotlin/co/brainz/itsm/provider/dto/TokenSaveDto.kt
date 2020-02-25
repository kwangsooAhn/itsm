package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenSaveDto(
        val instanceDto: InstanceDto,
        val processDto: ProcessDto,
        val tokenDto: TokenDto
) : Serializable
